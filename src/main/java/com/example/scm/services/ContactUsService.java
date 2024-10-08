package com.example.scm.services;

import java.util.List;

import com.example.scm.entities.ContactUs;

public interface ContactUsService {
    public ContactUs saveMessage(ContactUs message);

    public List<ContactUs> getAllMessages();

    public List<ContactUs> getMessagesByEmailId(String email);

}
