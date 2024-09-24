package com.example.scm.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    Logger log = LoggerFactory.getLogger(Helper.class);

    public String getEmailFromAuthentication(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) { // oauth login
            var clientId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = null;
            switch (clientId) {
                case "google":
                    username = oauth2User.getAttribute("email");
                    break;
                case "github":
                    username = oauth2User.getAttribute("email") == null
                            ? oauth2User.getAttribute("login") + "@gmail.com"
                            : oauth2User.getAttribute("email");
                    break;
                default:
                    log.error("unknown login method");
                    break;
            }
            return username;
        }

        System.out.println("Getting user data from local database");
        return authentication.getName();
    }
}
