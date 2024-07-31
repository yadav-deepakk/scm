package com.example.scm.services;

import java.util.List;
import java.util.Optional;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;

public interface ContactService {

    public Contact saveContact(Contact contact);

    public List<Contact> getAllContacts();

    public List<Contact> getAllContactsOfUser(User user);

    public Optional<Contact> getContactById(Long id);

    public Contact updateContact(Contact contact);

    public boolean deleteContact(Contact contact);

    public boolean deleteContact(Long id);
}
