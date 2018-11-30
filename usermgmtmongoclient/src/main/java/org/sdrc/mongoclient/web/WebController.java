package org.sdrc.mongoclient.web;

import java.security.Principal;
import java.util.Map;

import org.sdrc.usermgmt.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author subham
 *
 */
@RestController
public class WebController {

	@Autowired(required = false)
	private TokenStore tokenStore;

	@GetMapping("api/hello")
	public String hello() {
		return "hello client";
	}

	@GetMapping("check")
	public String hello1() {
		return "hello client";
	}

	@GetMapping("hello")
	@PreAuthorize("hasAuthority('dataentry_HAVING_write')")
	public String check() {
		return "hello method Level security-Active";
	}

	/**
	 * It extracts the user details from jwt access-token.
	 * 
	 * @param auth
	 * @return
	 */
	@ConditionalOnExpression("'${application.security.type}'=='jwt-both' OR "
			+ "'${application.security.type}'=='oauth2-resserver' "
			+ "OR '${application.security.type}'=='jwt-oauthserver'")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@Transactional
	public Map<String, Object> getExtraInfo(OAuth2Authentication auth) {
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
		return accessToken.getAdditionalInformation();
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@Transactional
	public UserModel principal(Principal principal) {

		return (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
