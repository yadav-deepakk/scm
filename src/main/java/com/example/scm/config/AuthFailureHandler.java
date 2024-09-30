package com.example.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        /*
         * ============== Other possible exceptions ================
         * DisabledException, UsernameNotFoundException, BadCredentialsException
         * AccountExpiredException, LockedException, CredentialsExpiredException,
         * NonceExpiredException,SessionAuthenticationException,
         * OAuth2AuthenticationException, PreAuthenticatedCredentialsNotFoundException,
         * ProviderNotFoundException, InsufficientAuthenticationException,
         * AuthenticationServiceException,
         * 
         */

    }

}
