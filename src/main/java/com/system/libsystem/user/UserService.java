package com.system.libsystem.user;

import com.system.libsystem.rest.registration.token.ConfirmationToken;
import com.system.libsystem.rest.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private static final int CARD_NUMBER_LENGTH = 10;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        final UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            final User user = new User();
            user.setUsername(userEntity.getUsername());
            user.setPassword(userEntity.getPassword());
            user.setEnabled(userEntity.isEnabled());
            return user;
        }
        else {
            throw new UsernameNotFoundException("Unable to find user " + username);
        }
    }
    
    public String registerUser(UserEntity userEntity) {

        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new IllegalStateException("Username already taken");
        }

        if (userEntity.getCardNumber().toString().length() != CARD_NUMBER_LENGTH) {
            throw new IllegalStateException("Invalid card number format");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, userEntity);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String username) {
        userRepository.findByUsername(username).setEnabled(true);
        loadUserByUsername(username).setEnabled(true);
        return userRepository.enableUser(username);
    }
}
