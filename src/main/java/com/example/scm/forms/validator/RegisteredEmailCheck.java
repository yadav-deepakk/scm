package com.example.scm.forms.validator;

import com.example.scm.service.impl.UserServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisteredEmailCheck implements ConstraintValidator<RegisteredEmail, String> {

	private final UserServiceImpl userService;

	public RegisteredEmailCheck(UserServiceImpl userService) {
		this.userService = userService;
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return this.userService.doesUserExistsByEmail(email);
	}

}
