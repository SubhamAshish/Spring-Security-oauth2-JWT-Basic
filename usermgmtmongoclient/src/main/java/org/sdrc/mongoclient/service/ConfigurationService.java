package org.sdrc.mongoclient.service;

import java.util.Map;

/**
 * @author subham
 *
 */
public interface ConfigurationService {

	String config(Map<String, Object> map);

	String createMongoOauth2Client();

}
