package com.example.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
    @NotBlank(message = "name is required.")
    @Size(min = 3, message = "minimum 3 characters required in name.")
    private String name;

    @NotBlank(message = "email is required.")
    @Email(message = "email is invalid.")
    private String email;

    @Size(min = 6, message = "password should be minimum 6 characters.")
    private String password;

    @Size(max = 1000, message = "about should be below 1000 characters.")
    private String about;

    @Size(min = 8, max = 12, message = "phone number should be between 8 and 12 digits.")
    private String phoneNumber;
}
