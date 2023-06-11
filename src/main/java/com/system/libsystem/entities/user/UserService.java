package com.system.libsystem.entities.user;

import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenEntity;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenService;
import com.system.libsystem.exceptions.cardnumber.CardNumberAlreadyTakenException;
import com.system.libsystem.exceptions.cardnumber.InvalidCardNumberFormatException;
import com.system.libsystem.exceptions.registration.InvalidEmailAddressFormat;
import com.system.libsystem.exceptions.registration.InvalidPasswordLengthException;
import com.system.libsystem.exceptions.registration.UsernameAlreadyTakenException;
import com.system.libsystem.exceptions.user.UserNotEnabledException;
import com.system.libsystem.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private static final String FIND_USER_EXCEPTION_LOG = "Unable to find user ";
    private static final String EMAIL_ADDRESS_REGEX_PATTERN = "^(.+)@(\\S+)$";
    private static final int CARD_NUMBER_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 25;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final SessionRegistry sessionRegistry;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));
        userEntity.setUsername(userEntity.getUsername());
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setEnabled(userEntity.isEnabled());
        return userEntity;
    }

    public UserEntity getUserById(Long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG
                + userId));
    }

    public UserEntity getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));
    }

    public String registerUser(UserEntity userEntity) {
        log.info("New user " + userEntity.getUsername() + " with id " + userEntity.getId() + " registration attempt");
        validateUserRegistrationData(userEntity);
        setOrUpdateUserPassword(userEntity, userEntity.getPassword());
        log.info("New user with id " + userEntity.getId() + " account created");
        final String token = UUID.randomUUID().toString();
        final ConfirmationTokenEntity confirmationTokenEntity = new ConfirmationTokenEntity(token, userEntity);
        confirmationTokenService.saveConfirmationToken(confirmationTokenEntity);
        return token;
    }

    public void enableUser(String username) {
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        log.info("New user with id " + userEntity.getId() + " enabled");
    }

    public void setOrUpdateUserPassword(UserEntity userEntity, String newPassword) {
        final String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
    }

    public void validateIfUserIsEnabled(UserEntity userEntity) {
        if (!userEntity.isEnabled()) {
            throw new UserNotEnabledException(userEntity.getId());
        }
    }

    public UserEntity getCurrentlyLoggedUser(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        return getUserByUsername(username);
    }

    private void validateUserRegistrationData(UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
            log.error("Username " + userEntity.getUsername() + " already taken");
            throw new UsernameAlreadyTakenException();
        }
        if (!isEmailAddressValid(userEntity.getUsername())) {
            throw new InvalidEmailAddressFormat();
        }
        if (userRepository.findByCardNumber(userEntity.getCardNumber()).isPresent()) {
            log.error("Card number " + userEntity.getCardNumber() + " already taken");
            throw new CardNumberAlreadyTakenException();
        }
        if (userEntity.getCardNumber().toString().length() != CARD_NUMBER_LENGTH) {
            throw new InvalidCardNumberFormatException();
        }
        if (userEntity.getPassword().length() < MIN_PASSWORD_LENGTH
                || userEntity.getPassword().length() > MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordLengthException();
        }
    }

    private static boolean isEmailAddressValid(String emailAddress) {
        return Pattern.compile(EMAIL_ADDRESS_REGEX_PATTERN).matcher(emailAddress).matches();
    }

}