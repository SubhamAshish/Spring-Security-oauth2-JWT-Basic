package org.sdrc.mongoclient;

import org.sdrc.usermgmt.core.annotations.EnableBasicWithMongoSecurityConfiguration;
import org.springframework.stereotype.Component;

/**
 * @author subham
 *	It activates user management
 */
@Component
//@EnableUserManagementWithOauth2MongoTokenStore
//@EnableOauth2MongoTokenStore
//@EnableUserManagementWithBasicMongoSecurityConfiguration
@EnableBasicWithMongoSecurityConfiguration
public class Loader {

}
