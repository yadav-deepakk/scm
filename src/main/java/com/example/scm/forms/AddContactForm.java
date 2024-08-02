package com.example.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class AddContactForm {
    @NotBlank(message = "Please enter contact name.")
    @Size(min = 3, max = 15, message = "Enter between 3 to 5 letters.")
    private String name;

    @NotBlank(message = "Please enter email.")
    @Email(message = "Enter a valid email address.")
    private String email;

    @NotBlank(message = "Enter a phone number.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Enter a valid phone number.")
    private String phoneNumber;

    @NotBlank(message = "Enter address.")
    @Size(max = 1000, message = "Enter less than 1000 letters.")
    private String address;

    @NotBlank(message = "Enter description.")
    @Size(max = 1000, message = "Enter less than 1000 letters.")
    private String description;

    private String picture;

    private String websiteLink;

    private String linkedInLink;

    private String profilePicture;

    private Boolean isFavourite;
}
