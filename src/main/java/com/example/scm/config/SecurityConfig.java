package com.example.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.scm.services.servicesImpl.UserServiceImpl;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Bean
    public DaoAuthenticationProvider authManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(CorsConfigurer::disable);
        http.csrf(CsrfConfigurer::disable);

        http.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests
                    .requestMatchers("/user/**").authenticated()
                    .anyRequest().permitAll();
        });

        http.formLogin(formLoginConfig -> {
            formLoginConfig
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .successForwardUrl("/user/profile")
                    .usernameParameter("email")
                    .passwordParameter("password");
        });

        http.logout(logoutConfig -> {
            logoutConfig
                    .logoutUrl("/do-logout")
                    .logoutSuccessUrl("/login?logout=true");
        });

        return http.build();
    }
}
