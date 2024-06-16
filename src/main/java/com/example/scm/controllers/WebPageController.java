package com.example.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class WebPageController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Logger log = LoggerFactory.getLogger(WebPageController.class);

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

		String message = "Registration successful.";
		MessageType type = MessageType.green;

		if (rBindingResult.hasErrors()) {
			log.info("Form has errors");
			Message msg = Message.builder().messageContent("You have errors, please correct and resubmit!")
					.messageType(MessageType.red).build();
			session.setAttribute("message", msg);
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

		Message msg = Message.builder().messageContent(message).messageType(type).build();
		session.setAttribute("message", msg);
		return new ModelAndView(new RedirectView("/signup"));
	}

}
