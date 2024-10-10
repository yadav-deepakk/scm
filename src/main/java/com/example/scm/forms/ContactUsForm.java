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
@AllArgsConstructor
@NoArgsConstructor
public class ContactUsForm {

    @NotBlank(message = "name can not be blank.")
    @Size(max = 100, message = "name should be less than 100 characters.")
    String name;

    @NotBlank(message = "email is required.")
    @Email(message = "email is invalid.")
    String email;

    @NotBlank(message = "subject can not be blank.")
    @Size(max = 100, message = "subject should be less than 100 characters.")
    String subject;

    @NotBlank(message = "subject can not be blank.")
    @Size(max = 1000, message = "message should be less than 1000 characters.")
    String message;

    Boolean isGuestUser;
}
