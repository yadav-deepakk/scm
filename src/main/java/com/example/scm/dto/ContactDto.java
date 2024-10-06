package com.example.scm.dto;

import lombok.Data;

@Data
public class ContactDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String description;
    private String websiteLink;
    private String linkedInLink;
    private String address;
    private Boolean isFavourite;
    private String picture;
}
