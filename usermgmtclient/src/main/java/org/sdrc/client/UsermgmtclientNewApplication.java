package org.sdrc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = { "org.sdrc.usermgmt.core", "org.sdrc.client" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EntityScan(basePackages = "org.sdrc.client.domain")
@EnableJpaRepositories(basePackages = { "org.sdrc.client.repository" })
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class,MongoRepositoriesAutoConfiguration.class,MongoDataAutoConfiguration.class})
public class UsermgmtclientNewApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UsermgmtclientNewApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UsermgmtclientNewApplication.class);
	}

}
