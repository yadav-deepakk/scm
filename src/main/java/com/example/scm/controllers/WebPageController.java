package com.example.scm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class WebPageController {

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
		return new ModelAndView("login", new HashMap<>());
	}

	// signup
	@RequestMapping("/signup")
	public ModelAndView signup() {
		log.info("Displaying signup page.");
		return new ModelAndView("signup", new HashMap<>());
	}

}
