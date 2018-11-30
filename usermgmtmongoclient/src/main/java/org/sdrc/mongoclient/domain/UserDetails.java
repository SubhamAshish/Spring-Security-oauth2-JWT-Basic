package org.sdrc.mongoclient.domain;

import org.sdrc.usermgmt.mongodb.domain.Account;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author subham
 *
 */
@Document
@Data
public class UserDetails {

	@Id
	private String id;

	private String address;

	private Long mblNumber;

	private Account account;

}