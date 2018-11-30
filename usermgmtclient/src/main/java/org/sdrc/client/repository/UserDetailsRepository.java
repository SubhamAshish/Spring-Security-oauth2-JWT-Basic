package org.sdrc.client.repository;

import org.sdrc.client.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author subham
 *
 */
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

}
