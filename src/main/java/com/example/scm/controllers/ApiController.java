package com.example.scm.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.scm.entities.Contact;
import com.example.scm.services.servicesImpl.ContactServiceImpl;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    public ContactServiceImpl contactService;

    @RequestMapping(path = "/contacts/{contactId}", method = RequestMethod.GET)
    public Contact returnContactById(@PathVariable Long contactId) {
        Optional<Contact> contact = contactService.getContactById(contactId);
        return contact.isPresent() ? contact.get() : null;
    }

}
