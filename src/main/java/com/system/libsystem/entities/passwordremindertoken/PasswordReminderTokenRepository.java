package com.system.libsystem.entities.passwordremindertoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordReminderTokenRepository extends JpaRepository<PasswordReminderTokenEntity, Long> {

    Optional<PasswordReminderTokenEntity> findByToken(String token);

}
