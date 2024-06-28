package com.example.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "user/dashboard";
    }

    @RequestMapping("/add-contact")
    public String addContact() {
        return "user/add-contact";
    }
}
