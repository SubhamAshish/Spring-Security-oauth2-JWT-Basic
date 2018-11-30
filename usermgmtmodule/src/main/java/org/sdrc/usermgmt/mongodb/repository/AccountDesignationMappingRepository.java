package org.sdrc.usermgmt.mongodb.repository;

import java.util.List;

import org.sdrc.usermgmt.mongodb.domain.Account;
import org.sdrc.usermgmt.mongodb.domain.AccountDesignationMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository(value="mongoAccountDesignationMappingRepository")
public interface AccountDesignationMappingRepository extends MongoRepository<AccountDesignationMapping, String>{

	List<AccountDesignationMapping> findByAccount(Account account);

}
