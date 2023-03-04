package com.system.libsystem.entities.user;

import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenEntity;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.system.libsystem.util.SharedConstants.INVALID_CARD_NUMBER_LOG;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    public static final String FIND_USER_EXCEPTION_LOG = "Unable to find user ";
    private static final int CARD_NUMBER_LENGTH = 10;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));

        userEntity.setUsername(userEntity.getUsername());
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setEnabled(userEntity.isEnabled());
        return userEntity;
    }

    public UserEntity getUserById(int userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG
                        + userId));
    }

    public UserEntity getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));
    }

    public String registerUser(UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }
        if (userEntity.getCardNumber().toString().length() != CARD_NUMBER_LENGTH) {
            throw new IllegalStateException(INVALID_CARD_NUMBER_LOG + " format");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
        log.info("New user with id " + userEntity.getId() + " account created");

        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationTokenEntity = new ConfirmationTokenEntity(token, userEntity);
        confirmationTokenService.saveConfirmationToken(confirmationTokenEntity);

        return token;
    }

    public int enableUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));
        userEntity.setEnabled(true);
        loadUserByUsername(username).setEnabled(true);
        log.info("New user with id " + userEntity.getId() + " enabled");
        return userRepository.enableUser(username);
    }

}
