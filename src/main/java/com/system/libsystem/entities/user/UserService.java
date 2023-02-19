package com.system.libsystem.entities.user;

import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenEntity;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.system.libsystem.util.SharedConstants.USER_EXCEPTION_LOG;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final int CARD_NUMBER_LENGTH = 10;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {

        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG + username));

        userEntity.setUsername(userEntity.getUsername());
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setEnabled(userEntity.isEnabled());
        return userEntity;
    }

    public String registerUser(UserEntity userEntity) {

        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }

        if (userEntity.getCardNumber().toString().length() != CARD_NUMBER_LENGTH) {
            throw new IllegalStateException("Invalid card number format");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);

        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationTokenEntity = new ConfirmationTokenEntity(token, userEntity);
        confirmationTokenService.saveConfirmationToken(confirmationTokenEntity);

        return token;
    }

    public int enableUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG + username));
        userEntity.setEnabled(true);
        loadUserByUsername(username).setEnabled(true);
        return userRepository.enableUser(username);
    }
}
