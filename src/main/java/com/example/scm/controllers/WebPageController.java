package com.example.scm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class WebPageController {
	@GetMapping("/home")
	public ModelAndView home() {
		System.out.println("Displaying Home Page");
		Map<String, String> attributes = new HashMap<>();
		attributes.put("projectName", "SCM");
		attributes.put("githubUsername", "yadav-deepak");
		attributes.put("githubLink", "https://github.com/yadav-deepakk");
		return new ModelAndView("home", attributes);
	}
}
