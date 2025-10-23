package com.example.scm.service.interfaces;

import com.example.scm.entities.User;

public interface MailingService {

    public boolean sendEmailVerificationMail(User user) throws Exception;

    public boolean sendForgetPasswordLink(User user) throws Exception;

    public boolean sendMail(String to, String subject, String body) throws Exception;

    public boolean sendMailHTML(String to, String subject, String body) throws Exception;

    public boolean sendMailWithAttachment(String to, String subject, String body) throws Exception;
}
