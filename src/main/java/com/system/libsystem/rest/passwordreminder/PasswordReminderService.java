package com.system.libsystem.rest.passwordreminder;

import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenEntity;
import com.system.libsystem.entities.passwordremindertoken.PasswordReminderTokenRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.system.libsystem.util.SharedConstants.USER_EXCEPTION_LOG;

@Service
@AllArgsConstructor
public class PasswordReminderService {

    private final MailSender mailSender;
    private final MailBuilder mailBuilder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordReminderTokenRepository passwordReminderTokenRepository;

    @Value("${server.password-reset.address}")
    private String passwordReminderAddress;

    public void remindPassword(PasswordReminderRequest passwordReminderRequest) {
        final UserEntity userEntity = userRepository.findByUsername(passwordReminderRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG));
        final String firstName = userEntity.getFirstName();
        final String lastName = userEntity.getLastName();
        final String token = UUID.randomUUID().toString();

        saveNewPasswordReminderToken(userEntity, token);

        mailSender.send(passwordReminderRequest.getUsername(), mailBuilder.getPasswordReminderMailBody
                (passwordReminderAddress + token, firstName, lastName,
                        passwordReminderRequest.getCardNumber().toString()), "Password reminder");
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity
                = passwordReminderTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new IllegalStateException("Unable to find requested token"));
        final int userId = passwordReminderTokenEntity.getUserEntity().getId();
        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG));

        String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
    }

    private void saveNewPasswordReminderToken(UserEntity userEntity, String token) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity = new PasswordReminderTokenEntity();
        passwordReminderTokenEntity.setToken(token);
        passwordReminderTokenEntity.setUserEntity(userEntity);
        passwordReminderTokenRepository.save(passwordReminderTokenEntity);
    }

    public String validatePasswordReminderToken(String token) {
        final PasswordReminderTokenEntity passwordReminderTokenEntity
                = passwordReminderTokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Unable to find requested password reminder token"));

        if (!isTokenFound(passwordReminderTokenEntity)) {
            return "Invalid token";
        }
        return null;
    }

    private boolean isTokenFound(PasswordReminderTokenEntity passwordReminderTokenEntity) {
        return passwordReminderTokenEntity != null;
    }

}
