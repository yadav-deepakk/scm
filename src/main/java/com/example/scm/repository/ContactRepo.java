package com.example.scm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;

public interface ContactRepo extends JpaRepository<Contact, Long> {
    Page<Contact> findByUser(User user, Pageable pageable);
}
