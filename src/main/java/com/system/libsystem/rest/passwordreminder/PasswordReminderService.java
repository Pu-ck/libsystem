package com.system.libsystem.rest.passwordreminder;

import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenEntity;
import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.system.libsystem.util.SharedConstants.FIND_USER_EXCEPTION_LOG;
import static com.system.libsystem.util.SharedConstants.INVALID_CARD_NUMBER_LOG;

@Service
@RequiredArgsConstructor
public class PasswordReminderService {

    private static final int PASSWORD_REMINDER_TOKEN_EXPIRATION_TIME = 8;

    private final MailSender mailSender;
    private final MailBuilder mailBuilder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordReminderTokenRepository passwordReminderTokenRepository;

    @Value("${server.password-reset.address}")
    private String passwordReminderAddress;

    public void remindPassword(PasswordReminderRequest passwordReminderRequest) {
        final UserEntity userEntity = userRepository.findByUsername(passwordReminderRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG));
        final String firstName = userEntity.getFirstName();
        final String lastName = userEntity.getLastName();
        final String token = UUID.randomUUID().toString();

        saveNewPasswordReminderToken(userEntity, token);

        if (Objects.equals(passwordReminderRequest.getCardNumber(), userEntity.getCardNumber())) {
            mailSender.send(passwordReminderRequest.getUsername(), mailBuilder.getPasswordReminderMailBody
                    (passwordReminderAddress + token, firstName, lastName,
                            passwordReminderRequest.getCardNumber().toString()), "Password reminder");
        } else {
            throw new IllegalStateException(INVALID_CARD_NUMBER_LOG);
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity
                = passwordReminderTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new IllegalStateException("Unable to find requested token"));
        final int userId = passwordReminderTokenEntity.getUserEntity().getId();
        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG));

        String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
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
    }

    public String validatePasswordReminderToken(String token) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity
                = passwordReminderTokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Unable to find requested password reminder token"));

        if (!isTokenFound(passwordReminderTokenEntity)) {
            return "Invalid token";
        } else if (isTokenExpired(passwordReminderTokenEntity)) {
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

}
