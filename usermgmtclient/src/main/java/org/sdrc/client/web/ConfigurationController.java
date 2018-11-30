package org.sdrc.client.web;

import org.sdrc.client.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/importArea")
	public String config(){
		
		return configurationService.importArea();
		
	}
	
	
}
