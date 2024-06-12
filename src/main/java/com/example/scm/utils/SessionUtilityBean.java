package com.example.scm.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionUtilityBean {
    public static void removeMessage() {
        try {
            @SuppressWarnings("null")
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            HttpSession session = request.getSession();
            if (session.getAttribute("message") != null) {
                session.removeAttribute("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
