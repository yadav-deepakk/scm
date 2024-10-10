package com.example.scm.services.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.scm.entities.ContactUs;
import com.example.scm.repository.ContactUsRepo;
import com.example.scm.services.ContactUsService;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepo contactUsRepo;

    public ContactUsServiceImpl(ContactUsRepo contactUsRepo) {
        this.contactUsRepo = contactUsRepo;
    }

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