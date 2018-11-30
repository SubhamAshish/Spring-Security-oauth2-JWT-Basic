package org.sdrc.usermgmt.core.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/*
 * This class gets active if and only if
 * @Condition TRUE
 * it activates oauth2,jwt resource server configuration
 * 
 */
@Configuration
@EnableResourceServer
@ConditionalOnExpression("'${application.security.type}'=='jwt-both' "
		+ "OR '${application.security.type}'=='oauth2-both' " + "OR '${application.security.type}'=='oauth2-resserver' "
		+ "OR '${application.security.type}'=='jwt-resserver'")
public class ResourceServer extends ResourceServerConfigurerAdapter {

	@Autowired
	private ConfigurableEnvironment configurableEnvironment;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {

		String resourceId = configurableEnvironment.getProperty("resource.server.resourceid");
		if (resourceId == null) {
			throw new NullPointerException(
					"Resource Id of resource server cannot be null.Please set property 'resource.server.resourceid'");
		}
		resources.resourceId(resourceId);
	}

	/*
	 * To alter any configuration or to allow any end-points mention the
	 * end-point here and make it permitAll()
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {

		if(!configurableEnvironment.containsProperty("security.allow.path")){
			throw new RuntimeException("property 'security.allow.path' is not set in application.properties");
		}
		
		List<String> property = Arrays.asList(configurableEnvironment.getProperty("security.allow.path").split(","))
				.stream().map(String::valueOf).collect(Collectors.toList());

		for (String str : property) {
			http.authorizeRequests().antMatchers(str).permitAll();
		}

		http.formLogin().disable().authorizeRequests().anyRequest().authenticated();

		http.csrf().disable();

	}

	@Primary
	@Bean
	@ConditionalOnExpression("'${application.security.type}'=='oauth2-resserver'")
	public RemoteTokenServices tokenService() {
		RemoteTokenServices tokenService = new RemoteTokenServices();

		String authUrl = configurableEnvironment.getProperty("oauth2.authserver.url");
		if (authUrl == null) {
			throw new NullPointerException(
					"Url for Authorization server cannot be null.Please set property 'oauth2.authserver.url'");
		}

		String clientId = configurableEnvironment.getProperty("oauth2.authserver.webclient");
		if (clientId == null) {
			throw new NullPointerException(
					"clientId Id of resource server cannot be null.Please set property 'oauth2.authserver.webclient'");
		}

		String clientPass = configurableEnvironment.getProperty("oauth2.authserver.clientpass");
		if (clientPass == null) {
			throw new NullPointerException(
					"Resource Id of resource server cannot be null.Please set property 'oauth2.authserver.clientpass'");
		}

		tokenService.setCheckTokenEndpointUrl(authUrl + "/oauth/check_token");
		tokenService.setClientId(clientId);
		tokenService.setClientSecret(clientPass);
		return tokenService;
	}
}
