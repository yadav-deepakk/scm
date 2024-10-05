package com.example.scm.services.servicesImpl;

import java.util.Date;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.scm.constants.AppConstants;
import com.example.scm.entities.User;
import com.example.scm.services.MailingService;

@Service
public class MailSenderImpl implements MailingService {

    private Random random = new Random();

    private final JavaMailSender mailSender;
    private final UserServiceImpl userService;

    public MailSenderImpl(JavaMailSender mailSender, UserServiceImpl userService) {
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @Override
    public boolean sendEmailVerificationMail(User user) throws Exception {
        String SUBJECT = "Email Verification From SCM2.0";
        int sixDigitOTP = 100000 + this.random.nextInt(900000); // Generate a random 6 digit number
        String nonce = java.util.UUID.randomUUID().toString();
        user.setEmailOTPIssuedAt(new Date());
        user.setEmailOTP(Integer.toString(sixDigitOTP));
        user.setNonce(nonce);

        String LINK = AppConstants.EMAIL_VERIFICATION_BASE_URL + "/auth/verify-email?nonce=" + nonce +
                "&email=" + user.getEmail() + "&otp=" + sixDigitOTP;

        String BODY = "click on given link to verify your email. (" + LINK + ")";
        userService.saveUser(user);
        this.sendMail(user.getEmail(), SUBJECT, BODY);
        return true;
    }

    @Override
    public boolean sendForgetPasswordLink(User user) throws Exception {
        String SUBJECT = "Password Reset Email From SCM2.0";

        int sixDigitOTP = 100000 + this.random.nextInt(900000); // Generate a random 6 digit number
        String nonce = java.util.UUID.randomUUID().toString();
        user.setForgetPassLinkIssuedAt(new Date());
        user.setForgetPassOTP(Integer.toString(sixDigitOTP));
        user.setNonce(nonce);
        userService.saveUser(user);
        String LINK = AppConstants.FORGET_PASS_BASE_URL + "/auth/reset-pass?nonce=" + nonce +
                "&email=" + user.getEmail() + "&otp=" + sixDigitOTP;

        String BODY = "Click on the link to reset your password. (" + LINK + ")";

        this.sendMail(user.getEmail(), SUBJECT, BODY);
        return true;
    }

    @Override
    public boolean sendMail(String to, String subject, String mailBody) throws Exception {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("scm@demomailtrap.com");
        msg.setSubject(subject);
        msg.setText(mailBody);
        this.mailSender.send(msg);
        return true;
    }

    @Override
    public boolean sendMailHTML(String to, String subject, String body) throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'sendMailHTML'");
    }

    @Override
    public boolean sendMailWithAttachment(String to, String subject, String body) throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'sendMailWithAttachment'");
    }

}
