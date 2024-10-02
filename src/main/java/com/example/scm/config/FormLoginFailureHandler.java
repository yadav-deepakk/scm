package com.example.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.scm.constants.AppConstants;
import com.example.scm.enums.MessageType;
import com.example.scm.models.Message;
import com.example.scm.services.servicesImpl.MailSenderImpl;
import com.example.scm.services.servicesImpl.UserServiceImpl;

@Component
public class FormLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FormLoginFailureHandler.class);

    private final UserServiceImpl userService;
    private final MailSenderImpl mailSender;

    public FormLoginFailureHandler(UserServiceImpl userService, MailSenderImpl mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        /*
         * ======================= Other possible exceptions ===========================
         * DisabledException, UsernameNotFoundException, BadCredentialsException
         * AccountExpiredException, LockedException, CredentialsExpiredException,
         * NonceExpiredException, SessionAuthenticationException,
         * OAuth2AuthenticationException, PreAuthenticatedCredentialsNotFoundException,
         * ProviderNotFoundException, InsufficientAuthenticationException,
         * AuthenticationServiceException
         * =============================================================================
         */
        HttpSession session = request.getSession();
        Message message = Message.builder().messageType(MessageType.red).build();
        String messageContent = "Your email is not verified. ";
        if (exception instanceof DisabledException) {
            String email = request.getParameter("email");
            log.info("Login attempted with email :: {}", email);
            // send verification email.
            try {
                mailSender.sendEmailVerificationMail(userService.getUserByEmail(email).get());
                messageContent += "Click on verification link send to your email to verify your email.(link will expire in 10 min)";
            } catch (Exception e) {
                log.error("Exception occured during verification mail send :: {}", e.getMessage());
                e.printStackTrace();
                messageContent += "Some error in sending verfication mail, please contact to "
                        + AppConstants.SUPPORT_TEAM_MAIL + " for support";
            }
            message.setMessageContent(messageContent);

        } else {
            System.out.println("Exception occured :: " + exception.getMessage());
            message.setMessageContent(exception.getMessage());
        }

        session.setAttribute("message", message);
        response.sendRedirect("/login?error");
    }

}
