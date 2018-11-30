package org.sdrc.usermgmt.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.sdrc.usermgmt.core.util.IUserManagementHandler;
import org.sdrc.usermgmt.exception.EmailMisMatchException;
import org.sdrc.usermgmt.exception.PasswordMismatchException;
import org.sdrc.usermgmt.exception.UserAlreadyExistException;
import org.sdrc.usermgmt.model.ChangePasswordModel;
import org.sdrc.usermgmt.model.ForgotPasswordModel;
import org.sdrc.usermgmt.model.Mail;
import org.sdrc.usermgmt.mongodb.domain.Account;
import org.sdrc.usermgmt.mongodb.domain.AccountDesignationMapping;
import org.sdrc.usermgmt.mongodb.domain.Designation;
import org.sdrc.usermgmt.mongodb.repository.AccountDesignationMappingRepository;
import org.sdrc.usermgmt.mongodb.repository.AccountRepository;
import org.sdrc.usermgmt.mongodb.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MongoUserManagementServiceImpl implements MongoUserManagementService {

	@Autowired(required=false)
	@Qualifier("mongoAccountDesignationMappingRepository")
	private AccountDesignationMappingRepository accountDesignationMappingRepository;

	@Autowired(required=false)
	@Qualifier("mongoAccountRepository")
	private AccountRepository accountRepository;

	@Autowired
	private ConfigurableEnvironment configurableEnvironment;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired(required=false)
	@Qualifier("mongoDesignationRepository")
	private DesignationRepository designationRepository;

	@Autowired(required=false)
	private IUserManagementHandler iuserManagementHandler;
	
	@Autowired(required=false)
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired(required=false)
	private MailService mailService;

	@Override
	@Transactional
	public ResponseEntity<String> createUser(Map<String, Object> map, Principal p) {

		Gson gson = new Gson();

		if (map.get("userName") == null || map.get("userName").toString().isEmpty())
			throw new RuntimeException("key : userName not found in map");

		if (map.get("designationIds") == null || map.get("designationIds").toString().isEmpty())
			throw new RuntimeException("key : designationIds not found in map");

		if (map.get("password") == null || map.get("password").toString().isEmpty())
			throw new RuntimeException("key : password not found in map");

		Account user = accountRepository.findByUserName(map.get("userName").toString());

		if (user != null) {

			throw new UserAlreadyExistException(configurableEnvironment.getProperty("username.duplicate.error"));

		}

		Account account = new Account();

		account.setUserName(map.get("userName").toString());
		account.setPassword(passwordEncoder.encode(map.get("password").toString()));

		if (map.get("email") != null || !map.get("email").toString().isEmpty()) {
			Account acc = accountRepository.findByEmail(map.get("email").toString());
			if (acc != null)
				throw new UserAlreadyExistException(configurableEnvironment.getProperty("email.duplicate.error"));
			account.setEmail(map.get("email").toString());
		}

		account = accountRepository.save(account);

		iuserManagementHandler.saveAccountDetails(map, account);

//		List<String> designationIds = Arrays.asList(map.get("designationIds").toString().split(",")).stream().map(String::valueOf).collect(Collectors.toList());
		
		List<Integer> designationIds = Arrays.asList(map.get("designationIds").toString().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
		
		List<Designation> designations = designationRepository.findBySlugIdIn(designationIds);

		List<AccountDesignationMapping> admList = new ArrayList<>();

		for (Designation designation : designations) {

			AccountDesignationMapping adm = new AccountDesignationMapping();
			adm.setAccount(account);
			adm.setDesignation(designation);
			admList.add(adm);

		}
		accountDesignationMappingRepository.save(admList);
		
		System.out.println(gson.toJson(configurableEnvironment.getProperty("user.create.success")));
		return new ResponseEntity<String>(gson.toJson(configurableEnvironment.getProperty("user.create.success")),HttpStatus.OK);
	}

	/**
	 * Updating password in database
	 */
	@Override
	@Transactional
	public ResponseEntity<String> changePassoword(ChangePasswordModel changePasswordModel) {
		/**
		 * using it to parse string as json
		 */
		Gson gson = new Gson();
		Account user = accountRepository.findByUserName(changePasswordModel.getUserName());
		try {
			/**
			 * check newpassword and confirm password is same or nots
			 */
			checkIfNewPasswordAndDbPasswordAreSame(changePasswordModel.getNewPassword(),changePasswordModel.getConfirmPassword(), user);

			/**
			 * check user has entered correct old password or not
			 */
			checkCorrectOldPassword(changePasswordModel.getOldPassword(), user.getPassword(), user);
			/**
			 * check new password is same as db password or not if same
			 */
			checkIfNewPasswordAndDbPasswordAreSame(changePasswordModel.getNewPassword(), user.getPassword(), user);
			/**
			 * encoding password
			 */
			user.setPassword(bCryptPasswordEncoder.encode(changePasswordModel.getNewPassword()));
			/**
			 * updating db
			 */
			accountRepository.save(user);

			log.info(configurableEnvironment.getProperty("password.update.success") + " for user : "
					+ user.getUserName());
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("password.update.success")),
					HttpStatus.OK);

		} catch (PasswordMismatchException e) {
			log.error("Action : change-password By {}: error while updating password! ", user.getUserName(), e);
			return new ResponseEntity<>(gson.toJson(e.getMessage()), HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error("Action : change-password By {}: error while updating password! ", user.getUserName(), e);
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("password.update.failure")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * check new password is same as db password or not if same
	 * 
	 * @param newPassword
	 * @param password
	 * @param user
	 */
	private void checkIfNewPasswordAndDbPasswordAreSame(String newPassword, String password, Account user) {
		if (bCryptPasswordEncoder.matches(newPassword, password)) {
			log.error(
					"Action : change-password By {}: error while updating password! : message : "
							+ configurableEnvironment.getProperty("new.password.previous.password"),
					user.getUserName());

			throw new PasswordMismatchException(configurableEnvironment.getProperty("new.password.previous.password"));
		}
	}

	/**
	 * check user has entered correct old password or not
	 * 
	 * @param oldPassword
	 * @param password
	 * @param user
	 */
	private void checkCorrectOldPassword(String oldPassword, String password, Account user) {
		if (!bCryptPasswordEncoder.matches(oldPassword, password)) {
			log.error("Action : change-password By {} : error while updating password! : message : ",
					user.getUserName(), configurableEnvironment.getProperty("password.not.matching"));
			throw new PasswordMismatchException(configurableEnvironment.getProperty("password.not.matching"));
		}
	}

	@Override
	@Transactional
	public ResponseEntity<String> validateOtp(String email, String otp) {
		/**
		 * using it to parse string as json
		 */
		Gson gson = new Gson();
		
		Account user = accountRepository.findByEmail(email);
		/**
		 * validating time of otp
		 */
		if ((user.getOtpGeneratedDateTime().getTime() + 10 * 60000) <= new Date().getTime()) {
			log.error("Action : validate-OTP By {} : error while validating Otp {} ",user.getUserName(),configurableEnvironment.getProperty("password.reset.time.expire"));
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("password.reset.time.expire")), HttpStatus.BAD_REQUEST);
		}
		/**
		 * validating number of attempts exceed 10 or not
		 */
		if (user.getInvalidAttempts() == 3) {
			log.error("Action : validate-OTP By {} : error while validating Otp {} ",user.getUserName(),configurableEnvironment.getProperty("password.reset.time.expire"));
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("invalid.otp.exceed.limit")),HttpStatus.BAD_REQUEST);
		}

		/**
		 * validating otp entered by user is correct or not
		 */
		if (user.getOtp().equals(otp)) {
			log.info("Action : validate-otp :  OTP Validated for user : {}",user.getUserName());
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("opt.valid")), HttpStatus.OK);
		} else {
			user.setInvalidAttempts((short) (user.getInvalidAttempts() + 1));
			
			if (3 - user.getInvalidAttempts() != 0) {
				if(3 - user.getInvalidAttempts()==1)
					return new ResponseEntity<>( gson.toJson("Invalid OTP! You have " + (3 - user.getInvalidAttempts())+" available attempt"),HttpStatus.BAD_REQUEST);
				else
					return new ResponseEntity<>( gson.toJson("Invalid OTP! You have " + (3 - user.getInvalidAttempts())+" available attempts"),HttpStatus.BAD_REQUEST);
			} else {
				log.error("Action : validate-OTP By {} : error while validating Otp {} ",user.getUserName(),configurableEnvironment.getProperty("password.reset.time.expire"));
				return new ResponseEntity<>( gson.toJson(configurableEnvironment.getProperty("invalid.otp.exceed.limit")),HttpStatus.BAD_REQUEST);	
				}
			}
	}

	@Override
	@Transactional
	public ResponseEntity<String> sendOtp(String userName) {
		/**
		 * using it to parse string as json
		 */
		Gson gson = new Gson();
		Account user = accountRepository.findByEmail(userName);
		try{
			
			if (user == null){
				return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("emailid.not.exist")), HttpStatus.NOT_FOUND);
			}
			/**
			 * generating 4 digit random otp
			 */
			Random random = new Random();
			String otp = String.format("%04d", random.nextInt(10000));

			/**
			 * updating user table with generated otp
			 */
			user.setOtp(otp);
			user.setOtpGeneratedDateTime(new Date());
			user.setInvalidAttempts((short) 0);

			accountRepository.save(user);

				/**
				 * creating mail object and seeting its required value to send mail
				 */
				Mail mail = new Mail();
				List<String> emailId = new ArrayList<String>();
				emailId.add(user.getEmail());
				mail.setToEmailIds(emailId);
				mail.setToUserName(user.getUserName());
				mail.setSubject("Forgot Password OTP");
				mail.setMessage(configurableEnvironment.getProperty("otp.send.message") + otp);
				mail.setFromUserName("Administrator");
				/**
				 * sending mail
				 */
				mailService.sendMail(mail);
				return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("otp.sent.success")), HttpStatus.OK);
				
			
		}catch (Exception e) {
			log.error("Action : generate-otp By {} : error while generating OTP! : ",user.getUserName(),e);
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("error.otp")), HttpStatus.CONFLICT);
		}
		
		
	}

	@Override
	@Transactional
	public ResponseEntity<String> forgotPassword(ForgotPasswordModel forgotPasswordModel) {
		/**
		 * using it to parse string as json
		 */
		Gson gson = new Gson();
		Account user = accountRepository.findByEmail(forgotPasswordModel.getEmailId());
		try {
			/**
			 * check if new password is equal to confirm password
			 */
			if (!forgotPasswordModel.getNewPassword().equals(forgotPasswordModel.getConfirmPassword())) {
				log.error("Action : change-password  : error while updating password! : message : ",
						configurableEnvironment.getProperty("password.not.matching"));
				throw new PasswordMismatchException(
						configurableEnvironment.getProperty("new.password.confirm.password.not.matching"));
			}
			/**
			 * validating otp entered by user is correct or not
			 */
			ResponseEntity<String> validateOtpMess = validateOtp(forgotPasswordModel.getEmailId(),forgotPasswordModel.getOtp());
			
			if (validateOtpMess.getStatusCodeValue() == 200) {
				user.setPassword(bCryptPasswordEncoder.encode(forgotPasswordModel.getNewPassword()));
				accountRepository.save(user);
				log.info("Action : forgot-password  message :: password updated successfull for user {}",
						user.getUserName());
				return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("password.forgot.success")),
						HttpStatus.OK);
			} else {
				return validateOtpMess;
			}
		} catch (Exception e) {
			log.error("Action : forgot-password By {}: error while creating new user with payload {} ",
					user.getUserName(), forgotPasswordModel, e);
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("password.reset.failure")),
					HttpStatus.CONFLICT);

		}
	}

	@Override
	@Transactional
	public ResponseEntity<String> updateUser(Map<String, Object> updateUserMap) {
		
		/**
		 * checking if account-id exist
		 */
		if(updateUserMap.get("id") == null || updateUserMap.get("id").toString().isEmpty()){
			throw new RuntimeException("key : id not found in updateUserMap");
		}
		
		/**
		 * checking if email-id already exist
		 */
		if(updateUserMap.get("email") != null || !updateUserMap.get("email").toString().isEmpty()){
			
			Account account = accountRepository.findByEmail(updateUserMap.get("email").toString());
			if(account!=null)
				throw new EmailMisMatchException(configurableEnvironment.getProperty("email.duplicate.error"));
			
		}

		Gson gson = new Gson();
		
		Account user = accountRepository.findById((String)updateUserMap.get("id"));
		
		try{
			
			iuserManagementHandler.updateAccountDetails(updateUserMap, user);
			log.info("Action : update-user :  user information updated successfully  for user : {}",user.getUserName());
			return new ResponseEntity<>(gson.toJson(configurableEnvironment.getProperty("user.update.success")), HttpStatus.OK);
			
		}catch (Exception e) {
			log.error("Action : update user : error : ",e);
			throw new EmailMisMatchException(configurableEnvironment.getProperty("email.duplicate.error"));
		}
		
	}

	@Override
	@Transactional
	public ResponseEntity<String> enableUserName(String userId) {
		Gson gson = new Gson();
		Account user = accountRepository.findById(userId);
		try{
				user.setEnabled(true);
				accountRepository.save(user);
				log.info("Action : enable-user ::{} for user name : {}",configurableEnvironment.getProperty("user.enable.success"),user.getUserName());
				return new ResponseEntity<String>(gson.toJson(configurableEnvironment.getProperty("user.enable.success")),HttpStatus.OK);
			
		}catch (Exception e) {
			log.error("Action : enable-user By{}: error while creating new user with userId {}",user.getUserName(),user.getId(),e);
			throw new RuntimeException();
		}
	}

	@Override
	@Transactional
	public ResponseEntity<String> disableUserName(String userId) {
		Gson gson = new Gson();
		Account user = accountRepository.findById(userId);
		try {
			user.setEnabled(false);
			accountRepository.save(user);
			return new ResponseEntity<String>(gson.toJson(configurableEnvironment.getProperty("user.disable.success")), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Action : enable-user By{}: error while creating new user with userId {}",user.getUserName(),user.getId(),e);
			throw new RuntimeException();
		}
	}

	@Override
	public List<Designation> getAllDesignations() {
		/**
		 * Fetching all the designations from DB in asc order
		 */
		List<Designation> desgList = designationRepository.findAllByOrderByIdAsc();
		/**
		 * filtering the role-name of ADMIN 
		 */
		List<Designation> role = desgList.stream().filter(desgName->!"ADMIN".equals(desgName.getName())).collect(Collectors.toList());
		return role;
	}

}
