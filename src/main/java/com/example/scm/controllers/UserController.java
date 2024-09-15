package com.example.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/profile")
    public String profile() {
        log.info("Displaiyng user profile page.");
        return "user/profile";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        log.info("Displaying user dashboard page.");
        return "user/dashboard";
    }

    @RequestMapping("/about")
    public String about() {
        log.info("Displaying user dashboard page.");
        return "user/about";
    }

}
