package com.example.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {

    @RequestMapping(path = "/email-verify", method = RequestMethod.GET)
    public String verifyEmail(@RequestParam String otp) {

        return new String();
    }

}
