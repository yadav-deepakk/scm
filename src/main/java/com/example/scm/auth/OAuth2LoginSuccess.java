package com.example.scm.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.scm.entities.User;
import com.example.scm.enums.Provider;
import com.example.scm.enums.UserRole;
import com.example.scm.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public @Component class OAuth2LoginSuccess implements AuthenticationSuccessHandler {

	private final UserServiceImpl userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("OAuth2LoginSuccess | onAuthenticationSuccess | OAuth2 Login successful.");

		DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

		User user = User.builder().rolesList(List.of(UserRole.NORMAL.toString())).emailVerified(true).enabled(true)
				.password(null).build();

		String clientId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

		switch (clientId) {
		case "google":
			oAuth2User.getAttributes().forEach((key, val) -> log.info(key + " : " + val));
			// Google Parameters: at_hash, sub, email_verified, iss, given_name, nonce,
			// picture, aud, azp, name, exp, family_name, iat, email.
			user.setEmail(oAuth2User.getAttribute("email"));
			user.setProfilePicture(oAuth2User.getAttribute("picture"));
			user.setName(oAuth2User.getAttribute("name"));
			user.setProviderUserId(oAuth2User.getName());
			user.setProviders(Provider.GOOGLE);
			user.setAbout("This account is created using google.");
			break;

		case "github":
			oAuth2User.getAttributes().forEach((key, val) -> log.info(key + " : " + val));
			// login, id, node_id, avatar_url, gravatar_id, url, html_url, followers_url,
			// following_url, gists_url, starred_url, subscriptions_url, organizations_url,
			// repos_url, events_url, received_events_url, type : User, site_admin, name,
			// company, blog, location, email, hireable, bio, twitter_username,
			// public_repos, public_gists, followers, following, created_at, updated_at,
			// private_gists, total_private_repos, owned_private_repos, disk_usage,
			// collaborators, two_factor_authentication, plan

			String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email")
					: oAuth2User.getAttribute("login") + "@gmail.com";
			String picture = oAuth2User.getAttribute("avatar_url");
			String name = oAuth2User.getAttribute("login");
			String providerUserId = oAuth2User.getName();

			user.setEmail(email);
			user.setProfilePicture(picture);
			user.setName(name);
			user.setProviderUserId(providerUserId);
			user.setProviders(Provider.GITHUB);
			user.setAbout("This account is created using github");
			
			break;

		default:
			log.error("OAuthAuthenicationSuccessHandler: Unknown provider");
			break;
		}

		boolean existingUser = userService.doesUserExistsByEmail(user.getEmail());
		if (!existingUser) {
			userService.saveUser(user);
			log.info("user saved:" + user.getEmail());
		}

		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
	}

}
