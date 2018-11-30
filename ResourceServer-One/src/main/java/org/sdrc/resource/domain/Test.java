package org.sdrc.resource.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Test {

	@Id
	private Integer id;

	private String name;
}
