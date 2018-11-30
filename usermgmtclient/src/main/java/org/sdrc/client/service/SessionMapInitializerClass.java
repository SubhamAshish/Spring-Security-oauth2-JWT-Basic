package org.sdrc.client.service;

import java.util.HashMap;
import java.util.Map;

import org.sdrc.client.domain.Area;
import org.sdrc.client.domain.UserDetails;
import org.sdrc.client.domain.UserDetailsAreaMapping;
import org.sdrc.client.repository.UserDetailsAreaMappingRepository;
import org.sdrc.client.repository.UserDetailsRepository;
import org.sdrc.usermgmt.core.util.IUserManagementHandler;
import org.sdrc.usermgmt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author subham
 *
 */
@Service
public class SessionMapInitializerClass implements IUserManagementHandler {

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private UserDetailsAreaMappingRepository userDetailsAreaMappingRepository;

	@Override
	@Transactional
	public Map<String, Object> sessionMap(Object account) {

		Map<String, Object> sessionMap = new HashMap<>();

		sessionMap.put("area", "odissa");
		sessionMap.put("areaId", "1243");
		return sessionMap;
	}

	@Override
	@Transactional
	public boolean saveAccountDetails(Map<String, Object> map, Object account) {

		try {

			if (map.get("address") == null || map.get("address").toString().isEmpty())
				throw new RuntimeException("key : address not found in map");

			if (map.get("mbl") == null || map.get("mbl").toString().isEmpty())
				throw new RuntimeException("key : mbl not found in map");

			if (map.get("areaId") == null || map.get("areaId").toString().isEmpty())
				throw new RuntimeException("key : areaId not found in map");

			UserDetails userDetails = new UserDetails();
//			userDetails.setAccount((Account)account);
			userDetails.setAddress(map.get("address").toString());
			userDetails.setMblNumber((Long) map.get("mbl"));

			userDetails = userDetailsRepository.save(userDetails);

			UserDetailsAreaMapping userDetailsAreaMapping = new UserDetailsAreaMapping();

			Area area = new Area((Integer) map.get("areaId"));

			userDetailsAreaMapping.setArea(area);
			userDetailsAreaMapping.setUser(userDetails);
			userDetailsAreaMappingRepository.save(userDetailsAreaMapping);
			return true;

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean updateAccountDetails(Map<String, Object> map, Object account) {

		return false;
	}

}
