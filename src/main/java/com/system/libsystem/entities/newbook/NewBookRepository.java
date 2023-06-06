package com.system.libsystem.entities.newbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewBookRepository extends JpaRepository<NewBookEntity, Long> {

}
