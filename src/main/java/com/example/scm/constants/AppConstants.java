package com.example.scm.constants;

public class AppConstants {
    // Email Verification and Password Change.
    public static final String EMAIL_VERIFICATION_BASE_URL = "http://localhost:8081";
    public static final String FORGET_PASS_BASE_URL = "http://localhost:8081";
    public static final Long EMAIL_VERIFICATION_ALLOWED_DELAY_MIN = 10L;
    public static final Long PASSWORD_CHANGE_ALLOWED_DELAY_MIN = 10L;
    public static final String SUPPORT_TEAM_MAIL = "support@scm20.com";

    // Contact image
    public static final int CONTACT_IMAGE_WIDTH = 500;
    public static final int CONTACT_IMAGE_HEIGHT = 500;
    public static final String CONTACT_IMAGE_CROP = "fill";

    // Pagination
    public static final int CONTACT_PAGE_SIZE = 3;
}
