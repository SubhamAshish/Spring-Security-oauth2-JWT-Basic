package org.sdrc.usermgmt.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sdrc.usermgmt.core.util.IUserManagementHandler;
import org.sdrc.usermgmt.model.UserModel;
import org.sdrc.usermgmt.mongodb.domain.Account;
import org.sdrc.usermgmt.mongodb.domain.AccountDesignationMapping;
import org.sdrc.usermgmt.mongodb.domain.Designation;
import org.sdrc.usermgmt.mongodb.domain.DesignationAuthorityMapping;
import org.sdrc.usermgmt.mongodb.repository.AccountDesignationMappingRepository;
import org.sdrc.usermgmt.mongodb.repository.AccountRepository;
import org.sdrc.usermgmt.mongodb.repository.DesignationAuthorityMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author subham
 *
 */
@ConditionalOnExpression("'${app.datasource.type}'=='MONGO'")
@Component
public class CustomMongoUserDetails 
{

	@Autowired(required=false)
	@Qualifier("mongoAccountRepository")
	private AccountRepository accountRepository;

	@Autowired(required=false)
	private IUserManagementHandler iuserManagementHandler;
	
	@Qualifier("mongoAccountDesignationMappingRepository")
	@Autowired(required=false)
	private AccountDesignationMappingRepository accountDesignationMappingRepository;
	
	@Autowired(required=false)
	@Qualifier("mongoDesignationAuthorityMappingRepository")
	private DesignationAuthorityMappingRepository designationAuthorityMappingRepository;

	@Transactional
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Account account = accountRepository.findByUserName(name);

		if (account == null) {
			throw new UsernameNotFoundException("Invalid username or password !");
		}

		// application user is responsible to provide its value
		Map<String, Object> sessionMap = iuserManagementHandler.sessionMap(account);

		Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
		grantedAuthority.add(new SimpleGrantedAuthority("dataentry_HAVING_write"));

		/**
		 * As one user can have multiple roles
		 */
		Set<Object> designationIds = new HashSet<>();

		Set<String> designations = new HashSet<>();

		/**
		 * Adding authority
		 * like @PreAuthorize("hasAuthority('feature_HAVING_permission')")
		 * 
		 */
		
		List<AccountDesignationMapping> accountDesignationMapping = accountDesignationMappingRepository.findByAccount(account);
		
		//fetch all the designation associated with- logged-in  user account
		List<Designation> desgList = new ArrayList<>();
		
		accountDesignationMapping.forEach(ad->{
			
			desgList.add(ad.getDesignation());
			
		});
		
		//fecth all the DesignationAuthorityMapping
		List<DesignationAuthorityMapping> designationAuthorityMapping = designationAuthorityMappingRepository.findByDesignationIn(desgList);
		
		
		designationAuthorityMapping.forEach(da -> {
									// adding role-ids
							designationIds.add(da.getDesignation().getId());
			
							// adding rolename
							designations.add(da.getDesignation().getName());
							grantedAuthority.add(new SimpleGrantedAuthority(da.getAuthority().getAuthority()));
				});
		
		
		return new UserModel(account.getUserName(), account.getPassword(), account.isEnabled(), !account.isExpired(),
				!account.isCredentialexpired(), !account.isLocked(), grantedAuthority, account.getId(),
				designationIds, designations,sessionMap,account.getEmail());
	}

}
