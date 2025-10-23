package com.example.scm.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.scm.entities.User;
import com.example.scm.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public @ControllerAdvice class RootController {

	private final UserServiceImpl userService;

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
