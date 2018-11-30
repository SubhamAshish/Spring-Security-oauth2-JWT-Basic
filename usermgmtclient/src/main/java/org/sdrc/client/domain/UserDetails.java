package org.sdrc.client.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.sdrc.usermgmt.domain.Account;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author subham
 *
 */
@Entity
@Data
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String address;

	private Long mblNumber;

//	@OneToOne
//	@JoinColumn(name = "acc_id_fk",unique=true)
//	private Account account;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserDetailsAreaMapping> userAreaMappings;

}
