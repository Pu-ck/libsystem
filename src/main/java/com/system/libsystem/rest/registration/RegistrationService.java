package com.system.libsystem.rest.registration;

import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenEntity;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.entities.user.UserType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;
    @Value("${server.confirmation.address}")
    private String userConfirmationAddress;
    @Value("${application.login.address}")
    private String loginPageAddress;
    @Value("${mail.admin}")
    private String adminMail;

    public String register(RegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(request.getPassword());
        userEntity.setUsername(request.getUsername());
        userEntity.setCardNumber(request.getCardNumber());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setEnabled(false);
        userEntity.setUserType(UserType.USER);

        LocalDateTime datetime = LocalDateTime.now();
        String registrationTime = datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        String token = userService.registerUser(userEntity);
        String confirmationAddress = userConfirmationAddress + token;

        mailSender.send(adminMail, mailBuilder.getAccountConfirmationMailBody(
                        request.getUsername(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getCardNumber().toString(),
                        registrationTime,
                        confirmationAddress),
                "New account registration request");
        mailSender.send(request.getUsername(), mailBuilder.getAccountRegisteredMailBody(
                        request.getUsername(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getCardNumber().toString()),
                "Your account has been created");

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenService.getToken(token).orElseThrow(() ->
                new IllegalStateException("Confirmation token not found"));

        userService.enableUser(
                confirmationTokenEntity.getUserEntity().getUsername());

        mailSender.send(confirmationTokenEntity.getUserEntity().getUsername(), mailBuilder.getAccountEnabledMailBody(
                confirmationTokenEntity.getUserEntity().getFirstName(),
                confirmationTokenEntity.getUserEntity().getLastName(),
                loginPageAddress), "Account enabled");

        return "Requested account was confirmed and enabled";
    }

}
