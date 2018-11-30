package org.sdrc.usermgmt.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * @author Subham Ashish(subham@sdrc.co.in)
 * 
 * This annotation enables spring security OAUTH2-JWT authentication,
 * And USERMANAGEMENT
 * 
 * Database used SQL
 *
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@EntityScan(basePackages = "org.sdrc.usermgmt.domain")
@EnableJpaRepositories(basePackages = { "org.sdrc.usermgmt.repository" })
@ComponentScan(basePackages = { "org.sdrc.usermgmt.service","org.sdrc.usermgmt.controller"})
public @interface EnableUserManagementWithJWTJPASecurityConfiguration {

}
