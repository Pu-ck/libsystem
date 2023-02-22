package com.system.libsystem.entities.borrowedbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBookEntity, Long> {

    List<BorrowedBookEntity> findAll();

    List<BorrowedBookEntity> findByUserId(int userId);

    Optional<BorrowedBookEntity> findById(int id);

}
