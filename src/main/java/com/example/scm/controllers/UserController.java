package com.example.scm.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.forms.AddContactForm;
import com.example.scm.helper.Helper;
import com.example.scm.models.Message;
import com.example.scm.services.ContactService;
import com.example.scm.services.servicesImpl.UserServiceImpl;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Helper helper;

    Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "user/dashboard";
    }

    @RequestMapping("/add-contact")
    public String addContactForm(Model model) {
        model.addAttribute("addContactForm", new AddContactForm());
        return "user/add-contact";
    }

    @RequestMapping(value = "/addContact-handler", method = RequestMethod.POST)
    public ModelAndView onAddContactFormSubmit(
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
            return new ModelAndView("user/add-contact");
        }

        // save contact to database.
        Contact contact = Contact.builder()
                .name(addContactForm.getName())
                .email(addContactForm.getEmail())
                .phoneNumber(addContactForm.getPhoneNumber())
                .address(addContactForm.getAddress())
                .picture(null)
                .description(addContactForm.getDescription())
                .isFavourite(addContactForm.getIsFavourite())
                .build();

        // get currently loggedInUser.
        String email = helper.getEmailFromAuthentication(authentication);
        User user = userService.getUserByEmail(email).get();

        contact.setUser(user);
        contactService.saveContact(contact);

        Message msg = Message.builder()
                .messageContent("Contact Added Succesfully.")
                .messageType(MessageType.green)
                .build();
        session.setAttribute("message", msg);

        return new ModelAndView("user/add-contact");
    }
}
