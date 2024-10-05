package com.example.scm.controllers;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.scm.constants.AppConstants;
import com.example.scm.entities.User;
import com.example.scm.enums.MessageType;
import com.example.scm.forms.ForgetPassForm;
import com.example.scm.forms.ResetPasswordForm;
import com.example.scm.models.Message;
import com.example.scm.services.servicesImpl.MailSenderImpl;
import com.example.scm.services.servicesImpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/auth")
public class AuthController {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private final UserServiceImpl userService;
    private final MailSenderImpl mailSender;
    private final PasswordEncoder encoder;

    public AuthController(UserServiceImpl userService, MailSenderImpl mailSender, PasswordEncoder encoder) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.encoder = encoder;
    }

    @RequestMapping(path = "/verify-email", method = RequestMethod.GET)
    public String verifyEmail(
            @RequestParam String nonce,
            @RequestParam String email,
            @RequestParam String otp,
            HttpSession session

    ) {
        log.info("Doing email verification for :: {}", email);
        try {
            User user = userService.getUserByEmail(email).get();
            // millisec -> sec
            Date otpIssueDate = user.getEmailOTPIssuedAt();
            float verifDelay = (float) Math.abs((new Date()).getTime() - otpIssueDate.getTime()) / (60 * 1000);

            if (verifDelay < AppConstants.EMAIL_VERIFICATION_ALLOWED_DELAY_MIN) {
                if (nonce.equals(user.getNonce()) && otp.equals(user.getEmailOTP())) {
                    user.setEmailVerified(true);
                    user.setEnabled(true);
                    userService.saveUser(user);
                    session.setAttribute("message", Message.builder()
                            .messageType(MessageType.green)
                            .messageContent(AppConstants.EMAIL_VERIF_SUCCESS)
                            .build());
                    log.info("Email verification succeeded for user :: {}", user.getEmail());
                } else {
                    session.setAttribute("message", Message.builder()
                            .messageType(MessageType.red)
                            .messageContent(AppConstants.EMAIL_VERIF_ERR_INVALID_LINK)
                            .build());
                    log.info("Email verification failed due to invalid link for user :: {}", user.getEmail());
                }
            } else {
                session.setAttribute("message", Message.builder()
                        .messageType(MessageType.red)
                        .messageContent(AppConstants.EMAIL_VERIF_ERR_EXPIRED_LINK)
                        .build());
                log.info("Email verification failed due to delay for user :: {}", user.getEmail());
            }
        } catch (Exception e) {
            log.info("Unknown Error in email verification :: ", e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", Message.builder()
                    .messageType(MessageType.red)
                    .messageContent("Unknown error in email verification.")
                    .build());
        }
        return "user/verify-email";
    }

    @RequestMapping(path = "/forget-pass", method = RequestMethod.GET)
    public String forgetPassword(Model model) {
        model.addAttribute("forgetPassForm", new ForgetPassForm());
        return "user/forget-pass.html";
    }

    @RequestMapping(path = "/forget-pass-handler", method = RequestMethod.POST)
    public String forgetPasswordHandler(
            @Valid @ModelAttribute("forgetPassForm") ForgetPassForm forgetPassForm,
            BindingResult rBindingResult,
            HttpSession session

    ) {
        if (rBindingResult.hasErrors()) {
            log.info("Forget pass email field has errors.");
            session.setAttribute("message", Message.builder()
                    .messageContent("You have errors, please correct and resubmit!")
                    .messageType(MessageType.red)
                    .build());
            return "user/forget-pass.html";
        }

        String email = forgetPassForm.getEmail();
        log.info("send forget pass link send to :: {}", email);
        User user = userService.getUserByEmail(email).get();
        try {
            mailSender.sendForgetPasswordLink(user);
            session.setAttribute("message", Message.builder()
                    .messageType(MessageType.green)
                    .messageContent(AppConstants.FORGET_PASS_EMAIL_SUCCESS)
                    .build());
        } catch (Exception e) {
            log.info("Error occured while sending forget pass link :: {}", e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", Message.builder()
                    .messageContent(AppConstants.FORGET_PASS_ERR_EMAIL_SENDING)
                    .messageType(MessageType.red)
                    .build());
        }
        return "user/forget-pass.html";
    }

    @RequestMapping(path = "/reset-pass", method = RequestMethod.GET)
    public String resetPassword(
            @RequestParam String nonce,
            @RequestParam String email,
            @RequestParam String otp,
            HttpSession session,
            Model model

    ) {
        log.info("reset-pass link clicked by user :: {}", email);
        try {
            model.addAttribute("otp", otp);
            model.addAttribute("nonce", nonce);
            model.addAttribute("email", email);
            User user = userService.getUserByEmail(email).get();
            // millisec -> sec
            Date otpIssueDate = user.getForgetPassLinkIssuedAt();
            float delay = (float) Math.abs((new Date()).getTime() - otpIssueDate.getTime()) / (60 * 1000);

            if (delay < AppConstants.PASSWORD_CHANGE_ALLOWED_DELAY_MIN) {
                if (nonce.equals(user.getNonce()) && otp.equals(user.getForgetPassOTP())) {
                    model.addAttribute("resetPasswordForm", new ResetPasswordForm());
                } else {
                    session.setAttribute("message", Message.builder()
                            .messageType(MessageType.red)
                            .messageContent(AppConstants.FORGET_PASS_ERR_INVALID_LINK)
                            .build());
                    log.info("Link is not valid to change password for user :: {}", user.getEmail());
                }

            } else {
                session.setAttribute("message", Message.builder()
                        .messageType(MessageType.red)
                        .messageContent(AppConstants.FORGET_PASS_ERR_EXPIRED_LINK)
                        .build());
                log.info("Pass change link has been expired.(only valid for {} min for user :: {})",
                        AppConstants.PASSWORD_CHANGE_ALLOWED_DELAY_MIN, user.getEmail());
            }

        } catch (Exception e) {
            log.info("Unknown error in displaying reset password page :: ", e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", Message.builder().messageType(MessageType.red)
                    .messageContent("Unknown error in displaying reset password page.").build());
        }
        return "user/reset-pass.html";
    }

    @RequestMapping(path = "/reset-pass-handler", method = RequestMethod.POST)
    public String resetPasswordHandler(
            @RequestParam String nonce,
            @RequestParam String email,
            @RequestParam String otp,
            @Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm form,
            BindingResult rBindingResult,
            HttpSession session

    ) {
        log.info("Password change requested by user :: {}", email);
        // log.info("Password: {} and confirm Password: {}", form.getNewPassword(),
        // form.getConfirmPassword());

        if (rBindingResult.hasErrors()) {
            log.info("form has errors please correct them and resubmit");
            session.setAttribute("message",
                    Message.builder().messageType(MessageType.red).messageContent(AppConstants.RESET_PASS_SUCCESS)
                            .build());
            return "user/reset-pass.html";
        }

        if (form.getNewPassword().equals(form.getConfirmPassword())) {

            User user = userService.getUserByEmail(email).get();

            Date otpIssueDate = user.getForgetPassLinkIssuedAt();
            float delay = (float) Math.abs((new Date()).getTime() - otpIssueDate.getTime()) / (60 * 1000);

            if (delay < AppConstants.PASSWORD_CHANGE_ALLOWED_DELAY_MIN) {
                if (nonce.equals(user.getNonce()) && otp.equals(user.getForgetPassOTP())) {
                    user.setPassword(encoder.encode(form.getNewPassword()));
                    session.setAttribute("message",
                            Message.builder()
                                    .messageType(MessageType.green)
                                    .messageContent(AppConstants.RESET_PASS_SUCCESS)
                                    .build());
                    userService.saveUser(user);
                } else {
                    session.setAttribute("message", Message.builder().messageType(MessageType.red)
                            .messageContent(AppConstants.FORGET_PASS_ERR_INVALID_LINK).build());
                    log.info("Link is not valid to change password for user :: {}", user.getEmail());
                }

            } else {
                session.setAttribute("message", Message.builder().messageType(MessageType.red)
                        .messageContent(AppConstants.FORGET_PASS_ERR_EXPIRED_LINK).build());
                log.info("Pass change link has been expired.(only valid for {} min for user :: {})",
                        AppConstants.PASSWORD_CHANGE_ALLOWED_DELAY_MIN, user.getEmail());
            }

        } else {
            log.info("error in changing password for user :: ", email);
            session.setAttribute("message",
                    Message.builder().messageType(MessageType.green).messageContent(AppConstants.RESET_PASS_SUCCESS)
                            .build());
        }

        return "user/reset-pass.html";
    }

}
