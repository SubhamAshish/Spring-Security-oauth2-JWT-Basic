package org.sdrc.usermgmt.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.sdrc.usermgmt.model.ChangePasswordModel;
import org.sdrc.usermgmt.model.ForgotPasswordModel;
import org.sdrc.usermgmt.mongodb.domain.Designation;
import org.springframework.http.ResponseEntity;

public interface MongoUserManagementService {

	ResponseEntity<String> createUser(Map<String, Object> map, Principal p);

	ResponseEntity<String> changePassoword(ChangePasswordModel changePasswordModel);

	ResponseEntity<String> validateOtp(String userName, String otp);

	ResponseEntity<String> sendOtp(String userName);

	ResponseEntity<String> forgotPassword(ForgotPasswordModel forgotPasswordModel);

	ResponseEntity<String> updateUser(Map<String, Object> updateUserMap);

	ResponseEntity<String> enableUserName(String userId);

	ResponseEntity<String> disableUserName(String userId);

	List<Designation> getAllDesignations();
}
