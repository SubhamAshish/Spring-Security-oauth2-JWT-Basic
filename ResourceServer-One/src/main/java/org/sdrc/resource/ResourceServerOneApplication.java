package org.sdrc.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"org.sdrc.usermgmt.core","org.sdrc.resource"})
public class ResourceServerOneApplication {

	@Bean
	RestTemplate testTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerOneApplication.class, args);
	}
}
