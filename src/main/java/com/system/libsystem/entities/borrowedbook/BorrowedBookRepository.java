package com.system.libsystem.entities.borrowedbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBookEntity, Long> {

    List<BorrowedBookEntity> findByUserId(Long userId);

    Optional<BorrowedBookEntity> findById(Long id);

}
