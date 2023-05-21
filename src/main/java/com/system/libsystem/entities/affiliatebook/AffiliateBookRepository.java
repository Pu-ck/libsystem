package com.system.libsystem.entities.affiliatebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliateBookRepository extends JpaRepository<AffiliateBook, Long> {

    Optional<AffiliateBook> findByBookIdAndAffiliateId(int bookId, int affiliateId);

}
