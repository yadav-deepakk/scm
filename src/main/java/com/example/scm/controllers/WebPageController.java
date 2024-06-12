package com.example.scm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.enums.Provider;
import com.example.scm.forms.LoginForm;
import com.example.scm.forms.SignUpForm;
import com.example.scm.models.Message;
import com.example.scm.services.servicesImpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
public class WebPageController {

	@Autowired
	private UserServiceImpl userService;

	Logger log = LoggerFactory.getLogger(WebPageController.class);

	// index/home page
	@RequestMapping({ "/", "/home" })
	public ModelAndView home() {
		log.info("Displaying Home Page.");
		Map<String, String> attributes = new HashMap<>();
		attributes.put("projectName", "SCM");
		attributes.put("githubUsername", "yadav-deepak");
		attributes.put("githubLink", "https://github.com/yadav-deepakk");
		return new ModelAndView("home", attributes);
	}

	// about
	@RequestMapping("/about")
	public ModelAndView about() {
		log.info("Displaying about page.");
		return new ModelAndView("about", new HashMap<>());
	}

	// services
	@RequestMapping("/services")
	public ModelAndView services() {
		log.info("Displaying services page.");
		return new ModelAndView("services", new HashMap<>());
	}

	// contact
	@RequestMapping("/contact")
	public ModelAndView contact() {
		log.info("Displaying contact page.");
		return new ModelAndView("contact", new HashMap<>());
	}

	// login
	@RequestMapping("/login")
	public ModelAndView login() {
		log.info("Displaying login page.");
		ModelAndView modelAndView = new ModelAndView("login");
		LoginForm loginFormData = new LoginForm();
		modelAndView.addObject("loginFormData", loginFormData);
		return modelAndView;
	}

	// signup
	@RequestMapping("/signup")
	public ModelAndView signup() {
		log.info("Displaying signup page.");
		SignUpForm signupFormData = new SignUpForm();
		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("signupFormData", signupFormData);
		return modelAndView;
	}

	@RequestMapping(value = "/register-user", method = RequestMethod.POST)
	public ModelAndView registerUserHandler(@ModelAttribute SignUpForm signUpFormData, HttpSession session) {
		log.info("Processing User Registration.");

		// validation of form.

		// save user to database if data is valid.
		User user = User.builder()
				.name(signUpFormData.getName())
				.email(signUpFormData.getEmail())
				.password(signUpFormData.getPassword())
				.phoneNumber(signUpFormData.getPhoneNumber())
				.about(signUpFormData.getAbout())
				.profilePicture("default.png")
				.providers(Provider.SELF)
				.build();

		userService.saveUser(user);

		Message msg = Message
				.builder()
				.messageContent("Registration successful.")
				.messageType(MessageType.green)
				.build();

		// adding attribute to the session.
		session.setAttribute("message", msg);

		// redirect user to same page
		RedirectView signupPage = new RedirectView("signup");
		return new ModelAndView(signupPage);
	}

	@RequestMapping(value = "/login-handler", method = RequestMethod.POST)
	public ModelAndView loginUserHandler(@ModelAttribute LoginForm loginFormData) {
		log.info("Processing User Login.");

		RedirectView homePageRedirection = new RedirectView("home");
		return new ModelAndView(homePageRedirection);
	}

}
