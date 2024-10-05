package com.example.scm.forms.customValidator;

import com.example.scm.services.servicesImpl.UserServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisteredEmailCheck implements ConstraintValidator<RegisteredEmail, String> {

    private final UserServiceImpl userService;

    public RegisteredEmailCheck(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (this.userService.doesUserExistsByEmail(email))
            return true;

        return false;
    }

}
