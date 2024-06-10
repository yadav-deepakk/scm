package com.example.scm.services;

import java.util.List;
import java.util.Optional;

import com.example.scm.entities.User;

public interface UserService {

    public User saveUser(User user);

    public List<User> getAllUsers();

    public Optional<User> getUserByUserId(Long id);

    public Optional<User> getUserByEmail(String email);

    public boolean doesUserExistsByUserId(Long id);

    public boolean doesUserExistsByEmail(String email);

    public User updateUserDetails(User user);

    public boolean deleteUser(Long id);

    public boolean deleteUser(User user);

}
