package com.example.scm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;

import java.util.List;

public interface ContactRepo extends JpaRepository<Contact, Long> {
    public List<Contact> findByUser(User user);
}
