package org.sdrc.usermgmt.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.sdrc.usermgmt.domain.Designation;
import org.sdrc.usermgmt.model.ChangePasswordModel;
import org.sdrc.usermgmt.model.ForgotPasswordModel;
import org.springframework.http.ResponseEntity;

public interface JPAUserManagementService {

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
