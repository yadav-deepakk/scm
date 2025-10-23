package com.example.scm.forms;

import com.example.scm.forms.validator.RegisteredEmail;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ForgetPassForm {
    @NotBlank(message = "Enter an email.")
    @RegisteredEmail(message = "this email is not registered with us.")
    private String email;
}
