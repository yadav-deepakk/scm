package com.example.scm.services.servicesImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;
import com.example.scm.repository.ContactRepo;
import com.example.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepo contactRepo;

    @Override
    public Contact saveContact(Contact contact) {
        Contact c = contactRepo.save(contact);
        return c;
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public List<Contact> getAllContactsOfUser(User user) {
        return contactRepo.findByUser(user);
    }

    @Override
    public Optional<Contact> getContactById(Long id) {
        return contactRepo.findById(id);
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact c = contactRepo.save(contact);
        return c;
    }

    @Override
    public boolean deleteContact(Long id) {
        contactRepo.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteContact(Contact contact) {
        contactRepo.delete(contact);
        return true;
    }

}
