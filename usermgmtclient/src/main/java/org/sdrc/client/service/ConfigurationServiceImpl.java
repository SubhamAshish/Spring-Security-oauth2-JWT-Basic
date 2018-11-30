package org.sdrc.client.service;

import java.util.ArrayList;
import java.util.List;

import org.sdrc.client.domain.AreaLevel;
import org.sdrc.client.repository.AreaLevelRepository;
import org.sdrc.usermgmt.domain.Authority;
import org.sdrc.usermgmt.domain.Designation;
import org.sdrc.usermgmt.domain.DesignationAuthorityMapping;
import org.sdrc.usermgmt.repository.AuthorityRepository;
import org.sdrc.usermgmt.repository.DesignationAuthorityMappingRepository;
import org.sdrc.usermgmt.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author subham
 *
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private AreaLevelRepository areaLevelRepository;

	@Autowired(required=false)
	private AuthorityRepository authorityRepository;

	@Autowired(required=false)
	private DesignationRepository designationRepository;

	@Autowired(required=false)
	private DesignationAuthorityMappingRepository designationAuthorityMappingRepository;

	@Override
	public String importArea() {

		AreaLevel areaLevel = new AreaLevel();
		areaLevel.setAreaLevelId(1);
		areaLevel.setAreaLevelName("NATIONAL");

		areaLevelRepository.save(areaLevel);

		areaLevel = new AreaLevel();
		areaLevel.setAreaLevelId(2);
		areaLevel.setAreaLevelName("STATE");

		areaLevelRepository.save(areaLevel);

		areaLevel = new AreaLevel();
		areaLevel.setAreaLevelId(3);
		areaLevel.setAreaLevelName("DISTRICT");

		areaLevelRepository.save(areaLevel);

		areaLevel = new AreaLevel();
		areaLevel.setAreaLevelId(4);
		areaLevel.setAreaLevelName("BLOCK");

		areaLevelRepository.save(areaLevel);

		areaLevel = new AreaLevel();
		areaLevel.setAreaLevelId(5);
		areaLevel.setAreaLevelName("PANCHAYAT");

		areaLevelRepository.save(areaLevel);

		areaLevel = new AreaLevel();
		areaLevel.setAreaLevelId(6);
		areaLevel.setAreaLevelName("VILLAGE");

		areaLevelRepository.save(areaLevel);

		// create designation

		List<Designation> designationList = new ArrayList<>();

		Designation desg = new Designation();
		desg.setCode("001");
		desg.setName("admin");
		designationList.add(desg);

		desg = new Designation();
		desg.setCode("002");
		desg.setName("CRP");
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

		return "succsessfull";
	}

}
