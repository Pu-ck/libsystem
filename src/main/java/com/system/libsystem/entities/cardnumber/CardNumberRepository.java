package com.system.libsystem.entities.cardnumber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardNumberRepository extends JpaRepository<CardNumberEntity, Long> {

    Optional<CardNumberEntity> findByCardNumber(long cardNumber);

}
