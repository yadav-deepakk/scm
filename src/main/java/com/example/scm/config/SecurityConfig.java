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

    @Autowired
    private OAuth2LoginSuccess oAuthSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

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
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll();
        });

        // http.formLogin(Customizer.withDefaults());

        http.formLogin(formLoginConfig -> {
            formLoginConfig
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .successForwardUrl("/user/profile")
                    .usernameParameter("email")
                    .passwordParameter("password");
            // .failureHandler(authFailureHandler);
        });

        http.logout(logoutConfig -> {
            logoutConfig
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true");
        });

        // http.oauth2Login(Customizer.withDefaults());
        // href="/oauth2/authorization/github" -> github signin
        // href="/oauth2/authorization/google" -> google signin

        http.oauth2Login(oauth -> {
            oauth
                    .loginPage("/login")
                    .successHandler(oAuthSuccessHandler);
        });

        return http.build();
    }
}
