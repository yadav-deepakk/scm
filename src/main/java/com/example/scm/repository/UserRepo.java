package com.example.scm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scm.entities.User;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
