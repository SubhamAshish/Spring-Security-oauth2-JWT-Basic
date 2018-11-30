package org.sdrc.usermgmt.mongodb.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class AccountDesignationMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6630116335212885026L;

	@Id
	private String id;

	private Account account;

	private Designation designation;

}