package org.sdrc.usermgmt.mongodb.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2959445609290107998L;

	@Id
	private String id;

	private String userName;

	private String password;

	private boolean enabled = true;

	private boolean credentialexpired = false;

	private boolean expired = false;

	private boolean locked = false;

	private String email;

	/**
	 * for forgot password
	 */
	private String otp;

	private short invalidAttempts = 0;

	private Date otpGeneratedDateTime;

	// constructor
	public Account() {
		super();
	}

	public Account(String id) {
		super();
		this.id = id;
	}
}