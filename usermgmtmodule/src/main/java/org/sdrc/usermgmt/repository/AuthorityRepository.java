package org.sdrc.usermgmt.repository;

import org.sdrc.usermgmt.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value="jpaAuthorityRepository")
public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

	Authority findByAuthority(String string);

}
