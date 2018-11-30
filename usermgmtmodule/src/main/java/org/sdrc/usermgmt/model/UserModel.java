package org.sdrc.usermgmt.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserModel extends User implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8439511551572574482L;

	private Object userId;

	private Set<Object> designationIds;

	private Set<String> designations;

	private Map<String, Object> sessionMap;
	
	private String email;

	/**
	 * *
	 * 
	 * @Description private constructors to use existing properties
	 */
	private UserModel(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

	}

	/***
	 * @Desription extra parameter has been passed here to be initialized and
	 *             return
	 */
	public UserModel(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			Object userId, Set<Object> roleIds, Set<String> roles, Map<String, Object> sessionMap,String email) {

		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

		this.userId = userId;
		this.designationIds = roleIds;
		this.designations = roles;
		this.sessionMap = sessionMap;
		this.email=email;
	}

	public Object getUserId() {
		return userId;
	}

	public void setUserId(Object userId) {
		this.userId = userId;
	}

	public Set<Object> getDesignationIds() {
		return designationIds;
	}

	public void setDesignationIds(Set<Object> designationIds) {
		this.designationIds = designationIds;
	}

	public Set<String> getDesignations() {
		return designations;
	}

	public void setDesignations(Set<String> designations) {
		this.designations = designations;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}
