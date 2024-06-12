package com.example.scm.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.scm.enums.Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String phoneNumber;
    private String profilePicture;

    @Builder.Default
    private boolean enabled = false;
    @Builder.Default
    private boolean phoneVerified = false;
    @Builder.Default
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Provider providers = Provider.SELF;
    private String providerUserId;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Contact> contacts = new ArrayList<>();
}