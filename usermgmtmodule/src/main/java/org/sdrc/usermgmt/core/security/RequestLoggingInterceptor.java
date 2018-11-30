 package org.sdrc.usermgmt.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Subham Ashish(subham@sdrc.co.in) Created Date:26-Sep-2018 5:29:33 PM
 * 
 *         it gets excuted before calling the method postAccessToken() of
 *         TokenEndPoint class For authenticate user request
 */
@Component
public class RequestLoggingInterceptor extends GenericFilterBean {

	private String clientId = "web";

	private String clientSecrte = "pass";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		/**
		 * verifying the grant type, shoud be password type
		 */
		if (httpRequest.getParameter("grant_type") != null
				&& httpRequest.getParameter("grant_type").equals("password")) {

			MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(httpRequest);

			String credentials = clientId + ":" + clientSecrte;
			String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

			mutableRequest.putHeader("Authorization", "Basic " + encodedCredentials);
			chain.doFilter(mutableRequest, response);

		} else {
			chain.doFilter(request, response);
		}

	}

}
