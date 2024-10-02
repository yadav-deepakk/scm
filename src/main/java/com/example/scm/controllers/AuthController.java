package com.example.scm.controllers;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.scm.constants.AppConstants;
import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.forms.ForgetPassForm;
import com.example.scm.models.Message;
import com.example.scm.services.servicesImpl.UserServiceImpl;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private final UserServiceImpl userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/verify-email", method = RequestMethod.GET)
    public String verifyEmail(@RequestParam String nonce, @RequestParam String email, @RequestParam String otp,
            Model model) {

        log.info("Doing email verification for :: {}", email);

        User user = userService.getUserByEmail(email).get();
        if (user.isEmailVerified()) {
            model.addAttribute("message", Message.builder()
                    .messageType(MessageType.green)
                    .messageContent("Your email has been already verified.")
                    .build());
            log.info("Email Already verified for user :: ", user.getEmail());
        }

        Date otpIssueDate = user.getEmailOTPIssuedAt();
        float timeDiff = (float) Math.abs((new Date()).getTime() - otpIssueDate.getTime()) / (60 * 1000); // msec -> min
        String nonceInDB = user.getNonce();
        String otpInDB = user.getEmailOTP();
        if (timeDiff < AppConstants.EMAIL_VERIFICATION_ALLOWED_DELAY_MIN
                && nonce.equals(nonceInDB) && otp.equals(otpInDB)) {
            user.setEmailVerified(true);
            user.setEnabled(true);
            userService.saveUser(user);
            model.addAttribute("message",
                    Message.builder().messageType(MessageType.green).messageContent("Your email verified.").build());
            log.info("Email verification succeeded for user :: {}", user.getEmail());
        }

        model.addAttribute("message",
                Message.builder().messageType(MessageType.green).messageContent("Your email verified.").build());
        log.info("Email verification failed for user :: {}", user.getEmail());

        return "user/verify-email";
    }

    @RequestMapping(path = "/forget-pass", method = RequestMethod.GET)
    public String forgetPassword(Model model) {
        model.addAttribute("forgetPassForm", model);
        return "user/forget-pass.html";
    }

    @RequestMapping(path = "/forget-pass-handler", method = RequestMethod.POST)
    public String forgetPasswordHandler(@ModelAttribute("forgetPassForm") ForgetPassForm fPassForm) {
        // User u = userService.getUserByEmail(email).get();
        return "user/forget-pass.html";
    }

    @RequestMapping(path = "/reset-pass", method = RequestMethod.GET)
    public String resetPassword(@RequestParam String nonce, @RequestParam String email, @RequestParam String otp) {
        return "user/reset-pass.html";
    }

    @RequestMapping(path = "/reset-pass-handler", method = RequestMethod.GET)
    public String resetPasswordHandler(@RequestParam String nonce, @RequestParam String email,
            @RequestParam String otp) {
        return "user/reset-pass.html";
    }

}
