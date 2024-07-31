package com.example.scm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.scm.entities.User;
import com.example.scm.services.servicesImpl.UserServiceImpl;

@SpringBootApplication
public class ScmApplication implements CommandLineRunner {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private PasswordEncoder passEncoder;

	Logger log = LoggerFactory.getLogger(ScmApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ScmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			// make a test user
			final String testEmail = new String("test@email.com");
			User testUser = User.builder()
					.email(testEmail)
					.password(passEncoder.encode("123321"))
					.name("Test User")
					.phoneNumber("+91-9999123123")
					.profilePicture("default.png")
					.about("A simple test user.")
					.build();

			if (!userService.doesUserExistsByEmail(testEmail))
				userService.saveUser(testUser);
			else
				log.info("User already exists with this email.");

		} catch (Exception e) {
			// error in creating test user.
			log.info("Exception Occured during test user creation");
			e.printStackTrace();
		}

	}

}
