package com.example.scm.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.enums.Provider;
import com.example.scm.forms.SignUpForm;
import com.example.scm.models.Message;
import com.example.scm.services.servicesImpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.scm.services.servicesImpl.MailSenderImpl;

@Controller
public class WebPageController {

	org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebPageController.class);

	private final UserServiceImpl userService;
	private final PasswordEncoder passwordEncoder;
	private final MailSenderImpl mailSender;

	public WebPageController(UserServiceImpl userService, PasswordEncoder passwordEncoder, MailSenderImpl mailSender) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
	}

	// index/home page
	@RequestMapping({ "/", "/home" })
	public String home(Model model) {
		log.info("Displaying Home Page.");
		model.addAttribute("projectName", "SCM");
		model.addAttribute("githubUsername", "yadav-deepak");
		model.addAttribute("githubLink", "https://github.com/yadav-deepakk");
		return "home";
	}

	// about
	@RequestMapping("/about")
	public String about() {
		log.info("Displaying about page.");
		return "about";
	}

	// services
	@RequestMapping("/services")
	public String services() {
		log.info("Displaying services page.");
		return "services";
	}

	// contact
	@RequestMapping("/contact")
	public String contact() {
		log.info("Displaying contact page.");
		return "contact";
	}

	// login
	@RequestMapping("/login")
	public String login() {
		log.info("Displaying login page.");
		return "login";
	}

	// signup
	@RequestMapping("/signup")
	public String signup(Model model) {
		log.info("Displaying signup page.");
		model.addAttribute("signupFormData", new SignUpForm());
		return "signup";
	}

	/*
	 * ===============================
	 * SingUp Form Submit Handler
	 * ===============================
	 */
	@RequestMapping(value = "/signup-handler", method = RequestMethod.POST)
	public ModelAndView registerUserHandler(@Valid @ModelAttribute("signupFormData") SignUpForm signUpFormData,
			BindingResult rBindingResult, HttpSession session) {
		log.info("Processing User Registration.");

		if (rBindingResult.hasErrors()) {
			log.info("Form has errors");
			session.setAttribute("message",
					Message.builder().messageContent("You have errors, please correct and resubmit!")
							.messageType(MessageType.red).build());
			return new ModelAndView("/signup");
		}

		// save user to database if data is valid.
		User user = User
				.builder()
				.name(signUpFormData.getName())
				.email(signUpFormData.getEmail())
				.password(passwordEncoder.encode(signUpFormData.getPassword()))
				.phoneNumber(signUpFormData.getPhoneNumber())
				.about(signUpFormData.getAbout())
				.profilePicture("default.png")
				.providers(Provider.SELF)
				.build();

		userService.saveUser(user);

		String msg = "Registration Successful. ";

		try {
			mailSender.sendEmailVerificationMail(user);
			msg += "Verification email has been send.";
		} catch (Exception e) {
			e.printStackTrace();
			log.info("error in sending verification email.");
			msg += "Error in sending verification email.";
		}
		session.setAttribute("message", Message.builder().messageContent(msg).messageType(MessageType.green).build());
		return new ModelAndView(new RedirectView("/signup"));
	}

}
