package com.example.scm.forms;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordForm {

    @Size(min = 8, max = 255, message = "password can be between 8 and 255 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.")
    private String newPassword;

    @Size(min = 8, max = 255, message = "password can be between 8 and 255 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.")
    private String confirmPassword;
}
