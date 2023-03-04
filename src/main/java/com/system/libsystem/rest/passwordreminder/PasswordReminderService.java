package com.system.libsystem.rest.passwordreminder;

import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenEntity;
import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenRepository;
import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.system.libsystem.util.SharedConstants.INVALID_CARD_NUMBER_LOG;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordReminderService {

    private static final int PASSWORD_REMINDER_TOKEN_EXPIRATION_TIME = 8;

    private final MailSender mailSender;
    private final MailBuilder mailBuilder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordReminderTokenRepository passwordReminderTokenRepository;
    private final UserService userService;
    private final BookUtil bookUtil;

    private final PasswordReminderTokenService passwordReminderTokenService;

    @Value("${server.password-reset.address}")
    private String passwordReminderAddress;

    public void remindPassword(PasswordReminderRequest passwordReminderRequest) {

        final UserEntity userEntity = userService.getUserByUsername(passwordReminderRequest.getUsername());
        final String token = UUID.randomUUID().toString();

        saveNewPasswordReminderToken(userEntity, token);

        if (bookUtil.isCardNumberValid(passwordReminderRequest.getCardNumber(), userEntity.getCardNumber())) {
            sendPasswordReminderMail(userEntity, passwordReminderRequest, token);
        } else {
            throw new IllegalStateException(INVALID_CARD_NUMBER_LOG);
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

        final PasswordReminderTokenEntity passwordReminderTokenEntity = passwordReminderTokenService
                .getPasswordReminderToken(resetPasswordRequest.getToken());
        final UserEntity userEntity = userService.getUserById(passwordReminderTokenEntity.getUserEntity().getId());

        String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
        log.info("New password has been set for user with id " + userEntity.getId());
    }

    private void saveNewPasswordReminderToken(UserEntity userEntity, String token) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity = new PasswordReminderTokenEntity();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, PASSWORD_REMINDER_TOKEN_EXPIRATION_TIME);

        passwordReminderTokenEntity.setToken(token);
        passwordReminderTokenEntity.setUserEntity(userEntity);
        passwordReminderTokenEntity.setExpiryDate(calendar.getTime());
        passwordReminderTokenRepository.save(passwordReminderTokenEntity);
        log.info("New password reminder token with id " + passwordReminderTokenEntity.getId() + " has been saved");
    }

    public String validatePasswordReminderToken(String token) {

        final PasswordReminderTokenEntity passwordReminderTokenEntity = passwordReminderTokenService
                .getPasswordReminderToken(token);

        if (!isTokenFound(passwordReminderTokenEntity)) {
            log.info("The password reminder token is invalid");
            return "Invalid token";
        } else if (isTokenExpired(passwordReminderTokenEntity)) {
            log.info("The password reminder token with id " + passwordReminderTokenEntity.getId() + " has expired");
            return "Expired token";
        }
        return null;
    }

    private boolean isTokenFound(PasswordReminderTokenEntity passwordReminderTokenEntity) {
        return passwordReminderTokenEntity != null;
    }

    private boolean isTokenExpired(PasswordReminderTokenEntity passwordReminderTokenEntity) {
        final Date passwordReminderTokenExpiryDateTime = passwordReminderTokenEntity.getExpiryDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final Date currentDateTime = calendar.getTime();

        return currentDateTime.compareTo(passwordReminderTokenExpiryDateTime) < 0;
    }

    private void sendPasswordReminderMail(UserEntity userEntity,
                                          PasswordReminderRequest passwordReminderRequest,
                                          String token) {
        mailSender.send(passwordReminderRequest.getUsername(), mailBuilder.getPasswordReminderMailBody(
                passwordReminderAddress + token,
                userEntity.getFirstName(),
                userEntity.getLastName(),
                passwordReminderRequest.getCardNumber().toString()), "Password reminder");
        log.info("New sendPasswordReminderMail message sent to " + userEntity.getUsername());
    }

}
