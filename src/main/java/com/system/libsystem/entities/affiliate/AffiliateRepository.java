package com.system.libsystem.entities.affiliate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AffiliateRepository extends JpaRepository<AffiliateEntity, Long> {

    Optional<AffiliateEntity> findByName(String name);

}
