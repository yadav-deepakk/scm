package com.example.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.scm.auth.FormLoginFailureHandler;
import com.example.scm.auth.OAuth2LoginSuccess;
import com.example.scm.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public @Configuration class SpringSecurityConfig {

	private final UserServiceImpl userDetailsService;
	private final OAuth2LoginSuccess oAuthSuccessHandler;
	private final FormLoginFailureHandler authFailureHandler;

	@Bean
	DaoAuthenticationProvider authManager() {
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
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors(CorsConfigurer::disable);
		http.csrf(CsrfConfigurer::disable);

		http.authorizeHttpRequests(authorizeHttpRequests -> 
			authorizeHttpRequests
				.requestMatchers("/user/**").authenticated()
				.requestMatchers("/api/**").authenticated()
				.anyRequest().permitAll()
		);

		// http.formLogin(Customizer.withDefaults());

		http.formLogin(formLoginConfig -> 
			formLoginConfig
				.loginPage("/login")
				.loginProcessingUrl("/authenticate")
		        .defaultSuccessUrl("/user/profile", true)
				.usernameParameter("email")
				.passwordParameter("password")
				.failureHandler(authFailureHandler)
		);

		http.logout(logoutConfig -> 
			logoutConfig
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout=true")
		);

		// http.oauth2Login(Customizer.withDefaults());
		// href="/oauth2/authorization/github" -> github signin
		// href="/oauth2/authorization/google" -> google signin

		http.oauth2Login(oauth -> 
			oauth
				.loginPage("/login")
				.successHandler(oAuthSuccessHandler)
		);

		return http.build();
	}
}
