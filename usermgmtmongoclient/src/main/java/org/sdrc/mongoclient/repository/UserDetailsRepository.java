package org.sdrc.mongoclient.repository;

import org.sdrc.mongoclient.domain.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author subham
 *
 */
public interface UserDetailsRepository extends MongoRepository<UserDetails, String> {

}
