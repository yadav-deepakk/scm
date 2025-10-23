package com.example.scm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.scm.entities.User;
import com.example.scm.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

	private final UserServiceImpl userService;
	private final PasswordEncoder passEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			// create a test user - only for testing purpose
			final String testEmail = new String("test@email.com");
			User testUser = User.builder().email(testEmail) //
					.password(passEncoder.encode("123321")) //
					.name("Test User") //
					.enabled(true)//
					.phoneNumber("+91-9999123123") //
					.profilePicture("default.png") //
					.about("A simple test user.") //
					.build();

			if (!userService.doesUserExistsByEmail(testEmail))
				userService.saveUser(testUser);
			else
				log.info("User already exists with this email.");

		} catch (Exception e) {
			// error in creating test user.
			log.info("Application || Exception Occured during test user creation | error : {}", e.getMessage(), e);
		}

	}

}
