package com.example.scm.services.servicesImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.scm.entities.User;
import com.example.scm.repository.UserRepo;
import com.example.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Override
    public String getEmailFromAuthentication(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) { // oauth login
            var clientId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = null;
            switch (clientId) {
                case "google":
                    username = oauth2User.getAttribute("email");
                    break;
                case "github":
                    username = oauth2User.getAttribute("email") == null
                            ? oauth2User.getAttribute("login") + "@gmail.com"
                            : oauth2User.getAttribute("email");
                    break;
                default:
                    log.error("unknown login method");
                    break;
            }
            return username;
        }

        System.out.println("Getting user data from local database");
        return authentication.getName();
    }
}