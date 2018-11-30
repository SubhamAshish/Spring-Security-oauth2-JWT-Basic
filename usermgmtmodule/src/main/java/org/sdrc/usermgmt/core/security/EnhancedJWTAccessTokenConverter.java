package org.sdrc.usermgmt.core.security;

import java.util.HashMap;
import java.util.Map;

import org.sdrc.usermgmt.model.UserModel;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
/*
 * @author azar
 * 
 * This class overrides the default accesstoken converter and sets the extra information,
 * retrieving the information from UserModel.
 */
public class EnhancedJWTAccessTokenConverter extends JwtAccessTokenConverter {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication.getOAuth2Request().getGrantType().equalsIgnoreCase("password")) {

			UserModel user = (UserModel) authentication.getPrincipal();

			final Map<String, Object> additionalInfo = new HashMap<String, Object>();

			additionalInfo.put("sessionMap", user.getSessionMap());
			additionalInfo.put("userId", user.getUserId());
			additionalInfo.put("designationIds", user.getDesignationIds());
			additionalInfo.put("emailId", user.getEmail());
			additionalInfo.put("designationNames", user.getDesignations());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		}
		accessToken = super.enhance(accessToken, authentication);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
		return accessToken;
	}
}
