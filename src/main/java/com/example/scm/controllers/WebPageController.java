package com.example.scm.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.scm.entities.ContactUs;
import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.enums.Provider;
import com.example.scm.forms.ContactUsForm;
import com.example.scm.forms.SignUpForm;
import com.example.scm.models.Message;
import com.example.scm.service.impl.MailSenderImpl;
import com.example.scm.service.impl.UserServiceImpl;
import com.example.scm.service.interfaces.ContactUsService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public @Controller class WebPageController {

	private final UserServiceImpl userService;
	private final PasswordEncoder passwordEncoder;
	private final MailSenderImpl mailSender;
	private final ContactUsService contactUsService;

	// index/home page
	@GetMapping({ "/", "/home" })
	public String home(Model model) {
		log.info("Displaying Home Page.");
		model.addAttribute("homePage", true);
		model.addAttribute("contactUsAction", "/contact-us-handler#contact");
		model.addAttribute("projectName", "SCM");
		model.addAttribute("githubUsername", "yadav-deepak");
		model.addAttribute("githubLink", "https://github.com/yadav-deepakk");
		model.addAttribute("contactUsForm", new ContactUsForm());
		return "home";
	}

	@PostMapping("/contact-us-handler")
	public String contactUsFormHandler(
			@RequestParam(defaultValue = "home") String currPage,
			@Valid @ModelAttribute("contactUsForm") ContactUsForm form,
			BindingResult rBindingResult,
			HttpSession session

	) {
		log.info("contact us form submitted from {} page and form data: {}", currPage, form.toString());
		if (rBindingResult.hasErrors()) {
			log.info("contact form has errors on {}.", currPage);
			session.setAttribute("message",
					Message.builder().messageContent("You have errors, please correct and resubmit!")
							.messageType(MessageType.red).build());
			return currPage;
		}

		ContactUs message = ContactUs.builder()
				.name(form.getName())
				.subject(form.getSubject())
				.email(form.getEmail())
				.message(form.getMessage())
				.build();

		contactUsService.saveMessage(message);
		return "redirect:/home";
	}

	// about
	@GetMapping("/about")
	public String about(Model model) {
		log.info("Displaying about page.");
		model.addAttribute("aboutPage", true);
		return "about";
	}

	// services
	@GetMapping("/services")
	public String services(Model model) {
		log.info("Displaying services page.");
		model.addAttribute("servicesPage", true);
		return "services";
	}

	// contact
	@GetMapping("/contact")
	public String contactUs(Model model) {
		log.info("Displaying contact-us page.");
		model.addAttribute("contactUsAction", "/contact-us-handler?currPage=contact");
		model.addAttribute("contactUsPage", true);
		return "contact";
	}

	// login
	@GetMapping("/login")
	public String login(Model model) {
		log.info("Displaying login page.");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication.getPrincipal().equals("anonymousUser"))) {
			return "redirect:/user/profile";
		}
		model.addAttribute("loginPage", true);
		return "login";
	}

	// signup
	@GetMapping("/signup")
	public String signup(Model model) {
		log.info("Displaying signup page.");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !(authentication.getPrincipal().equals("anonymousUser"))) {
			return "redirect:/home";
		}
		model.addAttribute("signupFormData", new SignUpForm());
		model.addAttribute("signupPage", true);
		return "signup";
	}

	/*
	 * ===============================
	 * SingUp Form Submit Handler
	 * ===============================
	 */
	@PostMapping("/signup-handler")
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
