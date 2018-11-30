package org.sdrc.mongoclient.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sdrc.mongoclientdetails.MongoClientDetails;
import org.sdrc.mongoclientdetails.repository.MongoClientDetailsRepository;
import org.sdrc.usermgmt.mongodb.domain.Authority;
import org.sdrc.usermgmt.mongodb.domain.Designation;
import org.sdrc.usermgmt.mongodb.domain.DesignationAuthorityMapping;
import org.sdrc.usermgmt.mongodb.repository.AuthorityRepository;
import org.sdrc.usermgmt.mongodb.repository.DesignationAuthorityMappingRepository;
import org.sdrc.usermgmt.mongodb.repository.DesignationRepository;
import org.sdrc.usermgmt.service.MongoUserManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * @author subham
 *
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private DesignationAuthorityMappingRepository designationAuthorityMappingRepository;

	@Autowired(required=false)
	private MongoUserManagementServiceImpl mongoUserManagementServiceImpl;

	@Autowired(required=false)
	private MongoClientDetailsRepository mongoClientDetailsRepository;

	@Override
	public String config(Map<String, Object> map) {

		// create designation
		List<Designation> designationList = new ArrayList<>();

		Designation desg = new Designation();
		desg.setCode("001");
		desg.setName("admin");
		desg.setSlugId(1);
		designationList.add(desg);

		desg = new Designation();
		desg.setCode("002");
		desg.setName("CRP");
		desg.setSlugId(2);
		designationList.add(desg);

		designationRepository.save(designationList);

		// create Authority

		List<Authority> authorityList = new ArrayList<>();

		Authority authority = new Authority();
		authority.setAuthority("usermgmt_HAVING_write");
		authority.setDescription("Allow user to manage usermanagement module");
		authorityList.add(authority);

		authority = new Authority();
		authority.setAuthority("dataentry_HAVING_write");
		authority.setDescription("Allow user to  dataentry module");
		authorityList.add(authority);

		authorityRepository.save(authorityList);

		// Designation-Authority Mapping

		List<DesignationAuthorityMapping> damList = new ArrayList<>();

		DesignationAuthorityMapping dam = new DesignationAuthorityMapping();

		dam.setAuthority(authorityRepository.findByAuthority("usermgmt_HAVING_write"));
		dam.setDesignation(designationRepository.findByCode("001"));
		damList.add(dam);

		dam = new DesignationAuthorityMapping();
		dam.setAuthority(authorityRepository.findByAuthority("dataentry_HAVING_write"));
		dam.setDesignation(designationRepository.findByCode("001"));
		damList.add(dam);

		dam = new DesignationAuthorityMapping();
		dam.setAuthority(authorityRepository.findByAuthority("dataentry_HAVING_write"));
		dam.setDesignation(designationRepository.findByCode("002"));
		damList.add(dam);

		designationAuthorityMappingRepository.save(damList);

		// create user
		mongoUserManagementServiceImpl.createUser(map, null);

		return "succsessfull";
	}

	@Override
	public String createMongoOauth2Client() {

		try {

			MongoClientDetails mongoClientDetails = new MongoClientDetails();

			HashSet<String> scopeSet = new HashSet<>();
			scopeSet.add("read");
			scopeSet.add("write");

			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("dashboard"));

			Set<String> authorizedGrantTypes = new HashSet<>();
			authorizedGrantTypes.add("refresh_token");
			authorizedGrantTypes.add("client_credentials");
			authorizedGrantTypes.add("password");

			Set<String> resourceIds = new HashSet<>();
			resourceIds.add("web-service");

			mongoClientDetails.setClientId("web");
			mongoClientDetails.setClientSecret("pass");
			mongoClientDetails.setScope(scopeSet);
			mongoClientDetails.setAccessTokenValiditySeconds(30000);
			mongoClientDetails.setRefreshTokenValiditySeconds(40000);
			mongoClientDetails.setAuthorities(authorities);
			mongoClientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
			mongoClientDetails.setResourceIds(resourceIds);

			mongoClientDetailsRepository.save(mongoClientDetails);
			return "success";

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

}
