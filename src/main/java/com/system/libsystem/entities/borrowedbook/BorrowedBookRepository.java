package com.system.libsystem.entities.borrowedbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBookEntity, Long> {

    List<BorrowedBookEntity> findAll();

    List<BorrowedBookEntity> findByUserId(int userId);

}
