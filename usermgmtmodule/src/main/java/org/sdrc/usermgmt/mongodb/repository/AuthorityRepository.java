package org.sdrc.usermgmt.mongodb.repository;

import org.sdrc.usermgmt.mongodb.domain.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(value="mongoAuthorityRepository")
public interface AuthorityRepository extends MongoRepository<Authority, String>{

	Authority findByAuthority(String string);

}
