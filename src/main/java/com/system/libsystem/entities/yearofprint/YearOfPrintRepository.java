package com.system.libsystem.entities.yearofprint;

import com.system.libsystem.entities.publisher.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearOfPrintRepository extends JpaRepository<PublisherEntity, Long>  {

}
