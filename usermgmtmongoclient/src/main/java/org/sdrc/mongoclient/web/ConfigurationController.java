package org.sdrc.mongoclient.web;


import java.util.Map;

import org.sdrc.mongoclient.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author subham
 *
 */
@RestController
@RequestMapping("/api")
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;
	
	@PostMapping("/config")
	public String config(@RequestBody Map<String,Object> map){
		
		return configurationService.config(map);
		
	}
	
	
	@GetMapping("/mongoClient")
	public String createMongoOauth2Client(){
		
		return configurationService.createMongoOauth2Client();
		
	}
	
}
