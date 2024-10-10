package com.example.scm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scm.entities.ContactUs;

public interface ContactUsRepo extends JpaRepository<ContactUs, Long> {

    public List<ContactUs> findByEmail(String email);
}
