package org.sdrc.usermgmt.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class AccountDesignationMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8855640852789971729L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "acc_id_fk")
	private Account account;

	@ManyToOne
	@JoinColumn(name = "designation_id_fk")
	private Designation designation;

}