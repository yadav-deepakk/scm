package com.example.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public @Controller class UserController {

	@GetMapping("/profile")
	public String profile() {
		log.info("Displaiyng user profile page.");
		return "user/profile";
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		log.info("Displaying user dashboard page.");
		return "user/dashboard";
	}

	@GetMapping("/about")
	public String about() {
		log.info("Displaying user dashboard page.");
		return "user/about";
	}

}
