package org.sdrc.usermgmt.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sdrc.usermgmt.core.util.IUserManagementHandler;
import org.sdrc.usermgmt.domain.Account;
import org.sdrc.usermgmt.domain.AccountDesignationMapping;
import org.sdrc.usermgmt.domain.DesignationAuthorityMapping;
import org.sdrc.usermgmt.model.UserModel;
import org.sdrc.usermgmt.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@Component
public class CustomJPAUserDetails {

	@Autowired(required = false)
	private AccountRepository accountRepository;

	@Autowired(required = false)
	private IUserManagementHandler iuserManagementHandler;

	@Transactional
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Account account = accountRepository.findByUserName(name);

		if (account == null) {
			throw new UsernameNotFoundException("Invalid username or password !");
		}

		// application user is responsible to provide its value
		Map<String, Object> sessionMap = iuserManagementHandler.sessionMap(account);

		Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();

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
		List<AccountDesignationMapping> accountDesignationMapping = account.getAccountDesignationMapping();

		accountDesignationMapping.forEach(ed -> {

			List<DesignationAuthorityMapping> designationAuthorityMapping = ed.getDesignation()
					.getDesignationAuthorityMapping();

			designationAuthorityMapping.forEach(da -> {

				// adding role-ids
				designationIds.add(da.getDesignation().getId());

				// adding rolename
				designations.add(da.getDesignation().getName());
				grantedAuthority.add(new SimpleGrantedAuthority(da.getAuthority().getAuthority()));
			});

		});

		return new UserModel(account.getUserName(), account.getPassword(), account.isEnabled(), !account.isExpired(),
				!account.isCredentialexpired(), !account.isLocked(), grantedAuthority, account.getId(), designationIds,
				designations, sessionMap, account.getEmail());
	}

}
