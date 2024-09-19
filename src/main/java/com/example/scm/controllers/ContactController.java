package com.example.scm.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.forms.AddContactForm;
import com.example.scm.helper.Helper;
import com.example.scm.models.Message;
import com.example.scm.services.ContactService;
import com.example.scm.services.servicesImpl.ImageServiceImpl;
import com.example.scm.services.servicesImpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/user/contacts")
public class ContactController {

        @Autowired
        private ContactService contactService;

        @Autowired
        private ImageServiceImpl imgService;

        @Autowired
        private UserServiceImpl userService;

        @Autowired
        private Helper helper;

        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContactController.class);

        @RequestMapping
        public String allContactList(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "name") String sortBy,
                        @RequestParam(defaultValue = "asc") String dir,
                        Model model,
                        Authentication authentication) {

                String email = helper.getEmailFromAuthentication(authentication);
                User user = userService.getUserByEmail(email).get();
                List<Contact> userContacts = new ArrayList<>();
                // userContacts = user.getContacts();
                contactService.getAllContactsOfUser(user, page, size, sortBy, dir);
                userContacts.forEach(contact -> {
                        log.info("{}\n{}\n{}\n", contact.getName(), contact.getPicture(), contact.getPhoneNumber());
                });
                model.addAttribute("userContactList", userContacts.size() > 0 ? userContacts : null);
                return "user/user-contacts";

        }

        @RequestMapping(path = "/add-contact", method = RequestMethod.GET)
        public String addContactForm(Model model) {
                log.info("Displaying Add Contact form.");
                model.addAttribute("addContactForm", new AddContactForm());
                return "user/add-contact";
        }

        @RequestMapping(path = "add", method = RequestMethod.POST)
        public String onAddContactFormSubmit(
                        Authentication authentication,
                        @Valid @ModelAttribute("addContactForm") AddContactForm addContactForm,
                        BindingResult rBindingResult,
                        HttpSession session) {
                log.info("Processing Add Contact form.");
                log.info("form data: {}", addContactForm);

                if (rBindingResult.hasErrors()) {
                        log.info("Add Contact Form has errors. \n errors : {}", rBindingResult.getAllErrors());
                        Message msg = Message.builder()
                                        .messageContent("Contact Form have errors, please correct and resubmit!")
                                        .messageType(MessageType.red)
                                        .build();
                        session.setAttribute("message", msg);
                        return "user/add-contact";
                }

                // save contact to database.
                Contact contact = Contact.builder()
                                .name(addContactForm.getName())
                                .email(addContactForm.getEmail())
                                .phoneNumber(addContactForm.getPhoneNumber())
                                .address(addContactForm.getAddress())
                                .picture(null)
                                .description(addContactForm.getDescription())
                                .websiteLink(addContactForm.getWebsiteLink())
                                .linkedInLink(addContactForm.getLinkedInLink())
                                .isFavourite(addContactForm.getIsFavourite())
                                .build();

                // get currently loggedInUser.
                String email = helper.getEmailFromAuthentication(authentication);
                User user = userService.getUserByEmail(email).get();

                contact.setUser(user);

                // saving image to cloudinary and retriving URL.
                log.info("image file name: {}", addContactForm.getContactImage().getOriginalFilename());

                if (addContactForm.getContactImage() != null && !addContactForm.getContactImage().isEmpty()) {
                        String filename = java.util.UUID.randomUUID().toString();
                        String fileURL = imgService.uploadImage(addContactForm.getContactImage(), filename);
                        contact.setPicture(fileURL);
                        contact.setCloudinaryImagePublicId(filename);
                }

                contactService.saveContact(contact);

                Message msg = Message.builder()
                                .messageContent("Contact Added Succesfully.")
                                .messageType(MessageType.green)
                                .build();
                session.setAttribute("message", msg);

                return "user/add-contact";
        }

}
