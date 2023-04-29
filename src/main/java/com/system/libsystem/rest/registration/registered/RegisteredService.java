package com.system.libsystem.rest.registration.registered;

import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenEntity;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.exceptions.ConfirmationTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisteredService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ResponseEntity<Void> validateRegistrationToken(String token, String username) {

        final ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException(token));
        final UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException(username));

        final int userId = confirmationTokenEntity.getUserEntity().getId();

        if (userEntity.getId() == userId) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
