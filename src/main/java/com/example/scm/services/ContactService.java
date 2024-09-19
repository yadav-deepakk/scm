package com.example.scm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;

public interface ContactService {

    public Contact saveContact(Contact contact);

    public List<Contact> getAllContacts();

    public Page<Contact> getAllContactsOfUser(User user, int page, int size, String sortBy, String direction);

    public Optional<Contact> getContactById(Long id);

    public Contact updateContact(Contact contact);

    public boolean deleteContact(Contact contact);

    public boolean deleteContact(Long id);
}
