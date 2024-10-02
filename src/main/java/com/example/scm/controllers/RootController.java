package com.example.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.scm.entities.User;
import com.example.scm.services.servicesImpl.UserServiceImpl;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserServiceImpl userService;

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RootController.class);

    @ModelAttribute
    public void addLoggedInUserToModel(Authentication authentication, Model model) {
        if (authentication == null)
            return;

        log.info("Adding user details to the model.");

        String email = userService.getEmailFromAuthentication(authentication);
        User user = userService.getUserByEmail(email).get();

        log.info("{}", user);
        log.info(user.getName());
        log.info(user.getEmail());

        model.addAttribute("loggedInUser", user);
    }
}
