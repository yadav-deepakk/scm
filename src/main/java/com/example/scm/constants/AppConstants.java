package com.example.scm.constants;

public class AppConstants {

	private AppConstants() {
	}

	// Email Verification
	public static final String SUPPORT_TEAM_MAIL = "support@scm20.com";
	public static final String EMAIL_VERIFICATION_BASE_URL = "http://localhost:8081";
	public static final Long EMAIL_VERIFICATION_ALLOWED_DELAY_MIN = 10L;
	public static final String EMAIL_VERIF_SUCCESS = "Your email verification has been done. You can access SCM app now by logging in.";
	public static final String EMAIL_VERIF_ERR_INVALID_LINK = "Invalid Link. Try to login and generate a new verification link.";
	public static final String EMAIL_VERIF_ERR_EXPIRED_LINK = "Link expired. Try to login and generate a new verification link.";

	// Forget Password
	public static final String FORGET_PASS_BASE_URL = "http://localhost:8081";
	public static final Long PASSWORD_CHANGE_ALLOWED_DELAY_MIN = 10L;
	public static final String FORGET_PASS_EMAIL_SUCCESS = "Password reset link has been send to your email.";
	public static final String FORGET_PASS_ERR_EMAIL_SENDING = "Error in sending password reset mail! Contact to our support team "
			+ SUPPORT_TEAM_MAIL;
	public static final String FORGET_PASS_ERR_EXPIRED_LINK = "Link has been expired (only valid for "
			+ PASSWORD_CHANGE_ALLOWED_DELAY_MIN + " min)";
	public static final String FORGET_PASS_ERR_INVALID_LINK = "Link is not valid to change password, try to generate a new link on lost password page.";
	public static final String RESET_PASS_SUCCESS = "Password has been changed successfully. You can now use new password to login.";

	// Contact image
	public static final int CONTACT_IMAGE_WIDTH = 500;
	public static final int CONTACT_IMAGE_HEIGHT = 500;
	public static final String CONTACT_IMAGE_CROP = "fill";

	// Pagination
	public static final int CONTACT_PAGE_SIZE = 3;
}
