package com.example.scm.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.scm.dto.ContactDto;
import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.services.servicesImpl.ContactServiceImpl;
import com.example.scm.services.servicesImpl.UserServiceImpl;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static ModelMapper modelMapper = new ModelMapper();

    private final ContactServiceImpl contactService;
    private final UserServiceImpl userService;

    public ApiController(ContactServiceImpl contactService, UserServiceImpl userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    @RequestMapping(path = "/contacts/{contactId}", method = RequestMethod.GET)
    public Contact getContactById(@PathVariable Long contactId) {
        Optional<Contact> contact = contactService.getContactById(contactId);
        return contact.isPresent() ? contact.get() : null;
    }

    @RequestMapping(path = "/xls/contacts", method = RequestMethod.GET)
    public ResponseEntity<List<ContactDto>> getAllContactsOfUser(Authentication authentication) {
        String email = userService.getEmailFromAuthentication(authentication);
        User user = userService.getUserByEmail(email).get();
        List<Contact> allContactsOfUser = contactService.getUserContacts(user);
        return ResponseEntity.ok(
                allContactsOfUser
                        .stream()
                        .map(contact -> modelMapper.map(contact, ContactDto.class)) // map each User to UserDTO
                        .collect(Collectors.toList()) // collect to List
        );
    }

}
