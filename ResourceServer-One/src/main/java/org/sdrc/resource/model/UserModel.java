package org.sdrc.resource.model;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModel {

	private Integer userId;
	private Map<String, Object> sessionMap;
	private Set<Integer> designationIds;

}
