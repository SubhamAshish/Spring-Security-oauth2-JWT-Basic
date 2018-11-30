package org.sdrc.usermgmt.core.security;

import org.sdrc.usermgmt.core.util.BasicAuthenticationPropertyCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * @author subham
 * It activate Basic Authentication security,
 * if and only if - @Conditional TRUE
 */
@Configuration
@Order(1)
@EnableWebSecurity
@Conditional(BasicAuthenticationPropertyCondition.class)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private  UserAuthenticationProvider userAuthenticationProvider;
	
	
	@Autowired
	private void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(userAuthenticationProvider);
	}
	
	
	/*
	 * To alter any configuration related to WEB-Application please update the configuration.
	 */
	 @Override
	 public void configure(HttpSecurity http) throws Exception {
	
		 http
			.httpBasic().and()
			.authorizeRequests()
				.antMatchers("/bypass/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf()
				.disable();
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		 
		 http.logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID","XSRF-TOKEN");
		 http.sessionManagement().sessionFixation().newSession();
		 http.sessionManagement().enableSessionUrlRewriting(false); // disable url rewriting, to pass session information in URL if cookie is disabled.
		 
	 }
	 
	 
}