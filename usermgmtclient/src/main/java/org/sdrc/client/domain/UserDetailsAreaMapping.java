package org.sdrc.client.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

/**
 * @author subham
 *
 */
@Data
@Entity
public class UserDetailsAreaMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_area_mapping_id")
	private Integer userAreaMappingId;

	@Column(name = "created_date", nullable = false)
	@CreationTimestamp
	private Timestamp createdDate;

	@ManyToOne
	@JoinColumn(name = "area_id_fk")
	private Area area;

	@ManyToOne
	@JoinColumn(name = "user_fk")
	private UserDetails user;

}
