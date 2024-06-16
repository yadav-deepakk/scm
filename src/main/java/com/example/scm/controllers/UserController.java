package com.example.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/profile")
    public String profile(Principal principal, Model model) {
        log.info("username : {}", principal.getName());
        model.addAttribute("username", principal.getName());
        return "user/profile";
    }
}
