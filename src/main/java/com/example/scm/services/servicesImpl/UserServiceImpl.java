package com.example.scm.services.servicesImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.scm.entities.User;
import com.example.scm.repository.UserRepo;
import com.example.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> getUserByUserId(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean doesUserExistsByUserId(Long id) {
        Optional<User> user = this.getUserByUserId(id);
        return user.isPresent();
    }

    @Override
    public boolean doesUserExistsByEmail(String email) {
        Optional<User> user = this.getUserByEmail(email);
        return user.isPresent();
    }

    @Override
    public User updateUserDetails(User user) {
        return userRepo.save(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        userRepo.deleteById(id);
        return this.doesUserExistsByUserId(id);
    }

    @Override
    public boolean deleteUser(User user) {
        userRepo.delete(user);
        return this.doesUserExistsByUserId(user.getUserId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such user found " + username));
    }
}