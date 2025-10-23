package com.example.scm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.scm.entities.ContactUs;
import com.example.scm.repository.ContactUsRepo;
import com.example.scm.service.interfaces.ContactUsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public @Service class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepo contactUsRepo;

    @Override
    public ContactUs saveMessage(ContactUs message) {
        ContactUs query = contactUsRepo.save(message);
        return query;
    }

    @Override
    public List<ContactUs> getAllMessages() {
        return contactUsRepo.findAll();
    }

    @Override
    public List<ContactUs> getMessagesByEmailId(String email) {
        return contactUsRepo.findByEmail(email);
    }
}