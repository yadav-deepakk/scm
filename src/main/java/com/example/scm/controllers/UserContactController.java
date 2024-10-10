package com.example.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.scm.constants.AppConstants;
import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.forms.ContactForm;
import com.example.scm.forms.SearchContactForm;
import com.example.scm.models.Message;
import com.example.scm.services.servicesImpl.ContactServiceImpl;
import com.example.scm.services.servicesImpl.ImageServiceImpl;
import com.example.scm.services.servicesImpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/user/contacts")
public class UserContactController {

    @Autowired
    private ContactServiceImpl contactService;

    @Autowired
    private ImageServiceImpl imgService;

    @Autowired
    private UserServiceImpl userService;

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserContactController.class);

    @RequestMapping
    public String allContactList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = AppConstants.CONTACT_PAGE_SIZE + "") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String dir,
            Model model,
            Authentication authentication

    ) {

        String email = userService.getEmailFromAuthentication(authentication);
        User user = userService.getUserByEmail(email).get();

        // List<Contact> userContacts = new ArrayList<>();
        // userContacts = user.getContacts();

        Page<Contact> currPageWithUserContacts = contactService.getAllContactsOfUser(user, page, size, sortBy, dir);

        model.addAttribute("pageSize", AppConstants.CONTACT_PAGE_SIZE);

        contactService.getAllContactsOfUser(user, page, size, sortBy, dir);
        currPageWithUserContacts.forEach(contact -> {
            log.info("{}\n{}\n{}\n", contact.getName(), contact.getPicture(), contact.getPhoneNumber());
        });

        model.addAttribute("userContactList",
                currPageWithUserContacts.getContent().size() > 0 ? currPageWithUserContacts : null);

        model.addAttribute("searchContactForm", new SearchContactForm());
        return "user/user-contacts";

    }

    @RequestMapping(value = "search")
    public String SearchContactsOfUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = AppConstants.CONTACT_PAGE_SIZE + "") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String dir,
            @Valid @ModelAttribute("searchContactForm") SearchContactForm searchForm,
            Authentication authentication,
            Model model

    ) {
        log.info("field:{}, keyword:{}", searchForm.getField(), searchForm.getKeyword());

        if (searchForm.getField() == null
                || searchForm.getKeyword() == null
                || searchForm.getField().equalsIgnoreCase("")
                || searchForm.getKeyword().equalsIgnoreCase("")) {
            log.info("{}", "No selection of field and keyword, so");
            log.info("{}", "redirecting to all contacts");
            return "redirect:/user/contacts";
        }

        String email = userService.getEmailFromAuthentication(authentication);
        User user = userService.getUserByEmail(email).get();

        Page<Contact> currPageWithUserContacts = null;

        if (searchForm.getField().equalsIgnoreCase("name")) {
            log.info("{}", "searching by name");
            currPageWithUserContacts = contactService.searchContactsOfUserByName(
                    user, searchForm.getKeyword(), page, size, sortBy, dir);
        } else if (searchForm.getField().equalsIgnoreCase("email")) {
            log.info("{}", "searching by email");
            currPageWithUserContacts = contactService.searchContactsOfUserByEmail(
                    user, searchForm.getKeyword(), page, size, sortBy, dir);
        } else if (searchForm.getField().equalsIgnoreCase("phoneNumber")) {
            log.info("{}", "searching by phoneNumber.");
            currPageWithUserContacts = contactService.searchContactsOfUserByPhoneNumber(
                    user, searchForm.getKeyword(), page, size, sortBy, dir);
        }

        log.info("currPageWithUserContacts {}", currPageWithUserContacts);
        model.addAttribute("searchContactForm", searchForm);
        if (currPageWithUserContacts != null) {
            model.addAttribute("userContactList",
                    currPageWithUserContacts.getContent().size() > 0 ? currPageWithUserContacts : null);
        } else
            model.addAttribute("userContactList", null);

        model.addAttribute("pageSize", AppConstants.CONTACT_PAGE_SIZE);

        return "user/search-results";
    }

    @RequestMapping(path = "/add-contact", method = RequestMethod.GET)
    public String addContactForm(Model model) {
        log.info("Displaying Add Contact form.");
        model.addAttribute("addContactForm", new ContactForm());
        return "user/add-contact";
    }

    @RequestMapping(path = "add", method = RequestMethod.POST)
    public String onAddContactFormSubmit(
            Authentication authentication,
            @Valid @ModelAttribute("addContactForm") ContactForm addContactForm,
            BindingResult rBindingResult,
            HttpSession session

    ) {
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
        String email = userService.getEmailFromAuthentication(authentication);
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
        return "redirect:/user/contacts/add-contact";
    }

    @RequestMapping(path = "/view/{contactId}", method = RequestMethod.GET)
    String updateContactView(@PathVariable("contactId") Long contactId, Model model) {
        Contact contact = contactService.getContactById(contactId).get();
        ContactForm contactForm = ContactForm.builder()
                .name(contact.getName())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .address(contact.getAddress())
                .description(contact.getDescription())
                .websiteLink(contact.getWebsiteLink())
                .linkedInLink(contact.getLinkedInLink())
                .picture(contact.getPicture())
                .isFavourite(contact.getIsFavourite())
                .build();
        model.addAttribute("updateContactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update-contact";
    }

    @RequestMapping(path = "/update-handler/{contactId}", method = RequestMethod.POST)
    String updateContactFormHandler(
            @PathVariable Long contactId,
            @Valid @ModelAttribute("updateContactForm") ContactForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session

    ) {
        log.info("update contact-form submit.");

        if (bindingResult.hasErrors()) {
            log.info("Form has errors");
            Message msg = Message.builder().messageContent("You have errors, please correct and resubmit!")
                    .messageType(MessageType.red).build();
            session.setAttribute("message", msg);
            return "user/update-contact";
        }

        Contact con = contactService.getContactById(contactId).get();

        con.setId(contactId);
        con.setName(form.getName());
        con.setEmail(form.getEmail());
        con.setPhoneNumber(form.getPhoneNumber());
        con.setAddress(form.getAddress());
        con.setDescription(form.getDescription());
        con.setIsFavourite(form.getIsFavourite());
        con.setWebsiteLink(form.getWebsiteLink());
        con.setLinkedInLink(form.getLinkedInLink());

        if (form.getContactImage() != null && !form.getContactImage().isEmpty()) {
            // delete previously uploaded image then upload current one.
            final String imageUUID = con.getCloudinaryImagePublicId();
            if (imageUUID != null && !imageUUID.equalsIgnoreCase("")) {
                imgService.deleteImage(imageUUID);
            }

            String filename = java.util.UUID.randomUUID().toString();
            String fileURL = imgService.uploadImage(form.getContactImage(), filename);
            con.setPicture(fileURL);
            con.setCloudinaryImagePublicId(filename);

        } else
            log.info("No Contact image was uploaded to update it.");

        contactService.saveContact(con);

        Message msg = Message.builder().messageContent("Contact has been updated!")
                .messageType(MessageType.green).build();
        session.setAttribute("message", msg);

        return "user/update-contact";
    }

    @RequestMapping(path = "/delete/{contactId}", method = RequestMethod.GET)
    public String deleteContactById(@PathVariable Long contactId, HttpSession session) {
        Contact contact = contactService.getContactById(contactId).get();
        final String imageUUID = contact.getCloudinaryImagePublicId();
        if (!(imageUUID == null || imageUUID.equalsIgnoreCase(""))) {
            imgService.deleteImage(imageUUID);
        }
        contactService.deleteContact(contactId);
        session.setAttribute("message", Message.builder()
                .messageContent("Contact is Deleted successfully !!")
                .messageType(MessageType.green).build());
        log.info("contactId {} deleted", contactId);
        return "redirect:/user/contacts";
    }
}
