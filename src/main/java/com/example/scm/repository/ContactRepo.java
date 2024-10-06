package com.example.scm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.scm.entities.Contact;
import com.example.scm.entities.User;

public interface ContactRepo extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    List<Contact> findByUser(User user);

    Page<Contact> findByUser(User user, Pageable pageable);

    Page<Contact> findByUserAndNameContaining(User user, String keyword, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user, String keyword, Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(User user, String keyword, Pageable pageable);
}
