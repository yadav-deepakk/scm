package com.example.scm.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scm.dto.ContactDto;
import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.service.impl.ContactServiceImpl;
import com.example.scm.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public @RestController class ApiController {

    private final ContactServiceImpl contactService;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper; 

    @GetMapping("/contacts/{contactId}")
    public Contact getContactById(@PathVariable Long contactId) {
    	log.info("ApiController || getContactById | contactId: {}", contactId); 
        Optional<Contact> contact = contactService.getContactById(contactId);
        return contact.isPresent() ? contact.get() : null;
    }

    @GetMapping("/xls/contacts")
    public ResponseEntity<List<ContactDto>> getAllContactsOfUser(Authentication authentication) {
    	log.info("ApiController || getAllContactsOfUser | authentication: {}", authentication); 
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
