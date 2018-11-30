package org.sdrc.client.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;

/**
 * @author subham
 * 
 *         This Entity class will keep all the area details
 * 
 * @since version 1.0.0.0
 *
 */
@Entity
@Table
@EqualsAndHashCode
public class Area implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558538811474305739L;

	@Id
	@Column(name = "area_id_pk", nullable = false)
	private Integer areaId;

	@Column(name = "area_name", nullable = false, length = 60)
	private String areaName;

	@Column(name = "area_code", nullable = false, length = 30)
	private String areaCode;

	@Column(name = "parent_area_id", nullable = false)
	private Integer parentAreaId;

	@Column(name = "live", nullable = false)
	private Boolean live;

	@Column(name = "created_by", length = 60)
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_date")
	private Timestamp createdDate;

	@ManyToOne
	@JoinColumn(name = "area_level_id_fk", nullable = false)
	private AreaLevel areaLevel;

	// ******** bi-directional one-to-many association to
	// UserAreaMapping***********

	@JsonIgnore
	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	private List<UserDetailsAreaMapping> userAreaMappings;

	public Area() {
		super();
	}

	public Area(Integer id) {
		super();
		this.areaId = id;
	}

}
