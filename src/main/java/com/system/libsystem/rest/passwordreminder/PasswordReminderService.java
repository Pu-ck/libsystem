package com.system.libsystem.rest.passwordreminder;

import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenEntity;
import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.cardnumber.UnableToAuthenticateCardNumberException;
import com.system.libsystem.exceptions.passwordreminder.PasswordReminderTokenExpiredException;
import com.system.libsystem.exceptions.passwordreminder.PasswordReminderTokenNotFoundException;
import com.system.libsystem.exceptions.user.UserNotEnabledException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordReminderService {

    private final MailSender mailSender;
    private final PasswordReminderTokenRepository passwordReminderTokenRepository;
    private final UserService userService;

    @Value("${application.login.address}")
    private String loginPageAddress;

    @Value("${application.password-reset.address}")
    private String passwordReminderAddress;

    public void remindPassword(PasswordReminderRequest passwordReminderRequest) {
        final UserEntity userEntity = userService.getUserByUsername(passwordReminderRequest.getUsername());
        final String token = UUID.randomUUID().toString();

        saveNewPasswordReminderToken(userEntity, token);

        if (!userEntity.isEnabled()) {
            throw new UserNotEnabledException(userEntity.getId());
        }
        if (BookUtil.isCardNumberValid(passwordReminderRequest.getCardNumber(), userEntity.getCardNumber())) {
            sendPasswordReminderMail(userEntity, passwordReminderRequest, token);
        } else {
            throw new UnableToAuthenticateCardNumberException();
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity = passwordReminderTokenRepository
                .findByToken(resetPasswordRequest.getToken()).orElseThrow(()
                        -> new PasswordReminderTokenNotFoundException(resetPasswordRequest.getToken()));
        final UserEntity userEntity = userService.getUserById(passwordReminderTokenEntity.getUserEntity().getId());

        if (userEntity.isEnabled()) {
            userService.setOrUpdateUserPassword(userEntity, resetPasswordRequest.getPassword());
            sendNewPasswordSetMail(userEntity);
            log.info("New password has been set for user with id " + userEntity.getId());
        } else {
            throw new UserNotEnabledException(userEntity.getId());
        }
    }

    private void saveNewPasswordReminderToken(UserEntity userEntity, String token) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity = new PasswordReminderTokenEntity();
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        passwordReminderTokenEntity.setToken(token);
        passwordReminderTokenEntity.setUserEntity(userEntity);
        passwordReminderTokenEntity.setExpiryDate(calendar.getTime());
        passwordReminderTokenRepository.save(passwordReminderTokenEntity);
        log.info("New password reminder token with id " + passwordReminderTokenEntity.getId() + " has been saved");
    }

    public ResponseEntity<Void> validatePasswordReminderToken(String token) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity = passwordReminderTokenRepository
                .findByToken(token).orElseThrow(() -> new PasswordReminderTokenNotFoundException(token));
        if (isTokenExpired(passwordReminderTokenEntity)) {
            throw new PasswordReminderTokenExpiredException(token, passwordReminderTokenEntity.getId());
        }
        return ResponseEntity.ok().build();
    }

    private boolean isTokenExpired(PasswordReminderTokenEntity passwordReminderTokenEntity) {
        final Date passwordReminderTokenExpiryDateTime = passwordReminderTokenEntity.getExpiryDate();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final Date currentDateTime = calendar.getTime();
        return currentDateTime.compareTo(passwordReminderTokenExpiryDateTime) >= 0;
    }

    private void sendPasswordReminderMail(UserEntity userEntity,
                                          PasswordReminderRequest passwordReminderRequest,
                                          String token) {
        mailSender.send(passwordReminderRequest.getUsername(), MailBuilder.getPasswordReminderMailBody(
                passwordReminderAddress + token,
                userEntity.getFirstName(),
                userEntity.getLastName(),
                passwordReminderRequest.getCardNumber().toString()), "Password reminder");
        log.info("New sendPasswordReminderMail message sent to " + userEntity.getUsername());
    }

    private void sendNewPasswordSetMail(UserEntity userEntity) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getNewPasswordSetMailBody(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                loginPageAddress), "New password successfully set");
        log.info("New sendNewPasswordSetMail message sent to " + userEntity.getUsername());
    }

}
