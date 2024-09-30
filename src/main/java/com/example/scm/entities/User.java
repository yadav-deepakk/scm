package com.example.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.scm.enums.Provider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
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
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Getter(value = AccessLevel.NONE)
    private String password;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String phoneNumber;
    private String profilePicture;

    @Builder.Default
    @Getter(value = AccessLevel.NONE)
    private boolean enabled = false;
    @Builder.Default
    private boolean phoneVerified = false;
    @Builder.Default
    private boolean emailVerified = false;
    private Date emailOTPIssuedAt;
    private String emailOTP;
    private String forgetPassLink;
    private String forgetPassLinkIssuedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Provider providers = Provider.SELF;
    private String providerUserId;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Contact> contacts = new ArrayList<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> rolesList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> roleCollection = rolesList.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return roleCollection;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}