package org.sdrc.usermgmt.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.sdrc.usermgmt.model.ChangePasswordModel;
import org.sdrc.usermgmt.model.ForgotPasswordModel;
import org.sdrc.usermgmt.service.JPAUserManagementService;
import org.sdrc.usermgmt.service.MongoUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author subham
 *
 */
@Controller
public class UserManagementController {

	@Autowired(required = false)
	private JPAUserManagementService jpaUserManagementService;

	@Autowired(required = false)
	private MongoUserManagementService mongoUserManagementService;

	@Autowired
	private ConfigurableEnvironment configurableEnvironment;

	/**
	 * create new user
	 */
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createUser(@RequestBody Map<String, Object> map, Principal p) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.createUser(map, p);
			break;

		case "MONGO":
			result = mongoUserManagementService.createUser(map, p);
			break;
		}

		return result;
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordModel changePasswordModel) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.changePassoword(changePasswordModel);
			;
			break;

		case "MONGO":
			result = mongoUserManagementService.changePassoword(changePasswordModel);
			;
			break;
		}

		return result;
	}

	/**
	 * Forgot password-
	 */

	// otp generation
	@RequestMapping(value = "/bypass/sendOtp")
	@ResponseBody
	public ResponseEntity<String> sendOtp(@RequestParam("userName") String userName) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.sendOtp(userName);
			break;

		case "MONGO":
			result = mongoUserManagementService.sendOtp(userName);
			break;
		}

		return result;

	}

	/**
	 * validating otp here
	 */
	@RequestMapping(value = "/bypass/validateOtp")
	@ResponseBody
	public ResponseEntity<String> validateOtp(@RequestParam("userName") String userName,
			@RequestParam("otp") String otp) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.validateOtp(userName, otp);
			break;

		case "MONGO":
			result = mongoUserManagementService.validateOtp(userName, otp);
			break;
		}
		return result;
	}

	@RequestMapping(value = "/bypass/forgotPassword", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordModel forgotPasswordModel) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.forgotPassword(forgotPasswordModel);
			break;

		case "MONGO":
			result = mongoUserManagementService.forgotPassword(forgotPasswordModel);
			break;
		}
		return result;
	}

	/**
	 * Update user
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateUser(@RequestBody Map<String, Object> updateUserMap) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.updateUser(updateUserMap);
			break;

		case "MONGO":
			result = mongoUserManagementService.updateUser(updateUserMap);
			break;
		}
		return result;

	}

	/**
	 * it unables user when user is disabled by admin
	 */
	@RequestMapping(value = "/enableUser")
	@ResponseBody
	public ResponseEntity<String> enableUser(@RequestParam("userId") String userId) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.enableUserName(userId);
			break;

		case "MONGO":
			result = mongoUserManagementService.enableUserName(userId);
			break;
		}
		return result;

	}

	/**
	 * it disable users whenever admin wants to disable
	 */
	@RequestMapping(value = "/disableUser")
	@ResponseBody
	public ResponseEntity<String> disableUser(@RequestParam("userId") String userId) {

		ResponseEntity<String> result = null;

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");

		switch (dataSourceType) {

		case "SQL":
			result = jpaUserManagementService.disableUserName(userId);
			break;

		case "MONGO":
			result = mongoUserManagementService.disableUserName(userId);
			break;
		}
		return result;

	}

	/**
	 * get all designations, except admin (from sql database)
	 */
	@RequestMapping(value = "/getAllDesignations")
	@ResponseBody
	public List<?> getAllDesignations() {

		String dataSourceType = configurableEnvironment.getProperty("app.datasource.type");
		
		switch(dataSourceType){
		
		case "SQL" :
			return jpaUserManagementService.getAllDesignations();
			
		case "MONGO":
			return mongoUserManagementService.getAllDesignations();
		
		}
		
		
		throw new RuntimeException("property 'app.datasource.type' is undefined");

	}
	
//	/**
//	 * get all designations, except admin  (from mongo database)
//	 */
//	@RequestMapping(value = "/getAllDesignation")
//	@ResponseBody
//	public List<org.sdrc.usermgmt.mongodb.domain.Designation> getAllDesignation() {
//
//		return mongoUserManagementService.getAllDesignations();
//		
//
//	}

}
