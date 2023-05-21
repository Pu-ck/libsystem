package com.system.libsystem.rest.registration;

import com.system.libsystem.entities.cardnumber.CardNumberEntity;
import com.system.libsystem.entities.cardnumber.CardNumberRepository;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenEntity;
import com.system.libsystem.entities.confirmationtoken.ConfirmationTokenService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.cardnumber.CardNumberNotFoundException;
import com.system.libsystem.exceptions.peselnumber.UnableToAuthenticatePeselNumberException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.util.HashingUtil;
import com.system.libsystem.util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final CardNumberRepository cardNumberRepository;
    private final UserService userService;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;

    @Value("${server.confirmation.address}")
    private String userConfirmationAddress;
    @Value("${application.login.address}")
    private String loginPageAddress;
    @Value("${mail.sender.admin}")
    private String adminMail;

    public ResponseEntity<RegistrationResponse> register(RegistrationRequest registrationRequest) {
        if (isCardNumberAlreadyRegistered(registrationRequest)) {
            UserEntity userEntity = new UserEntity();
            setUserDetails(registrationRequest, userEntity);

            final LocalDateTime datetime = LocalDateTime.now();
            final String registrationTime = datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            final String token = userService.registerUser(userEntity);
            final String confirmationAddress = userConfirmationAddress + token;

            sendAccountConfirmationMail(registrationRequest, registrationTime, confirmationAddress);
            sendAccountRegistrationMail(registrationRequest);
            log.info("New account created for user with id " + userEntity.getId());

            RegistrationResponse registrationResponse = new RegistrationResponse();
            registrationResponse.setToken(token);

            return ResponseEntity.ok(registrationResponse);
        } else {
            log.error("Unable to authenticate card number " + registrationRequest.getCardNumber() + " and PESEL number" +
                    registrationRequest.getPeselNumber());
            throw new UnableToAuthenticatePeselNumberException();
        }
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenService.getToken(token).orElseThrow(() ->
                new IllegalStateException("Confirmation token not found"));
        userService.enableUser(confirmationTokenEntity.getUserEntity().getUsername());
        sendAccountEnabledMail(confirmationTokenEntity);
    }

    private void setUserDetails(RegistrationRequest registrationRequest, UserEntity userEntity) {
        userEntity.setPassword(registrationRequest.getPassword());
        userEntity.setUsername(registrationRequest.getUsername());
        userEntity.setCardNumber(registrationRequest.getCardNumber());
        userEntity.setFirstName(registrationRequest.getFirstName());
        userEntity.setLastName(registrationRequest.getLastName());
        userEntity.setEnabled(false);
        userEntity.setUserType(UserType.USER);
    }

    private boolean isCardNumberAlreadyRegistered(RegistrationRequest registrationRequest) {
        final CardNumberEntity cardNumberEntity = cardNumberRepository.findByCardNumber(registrationRequest
                .getCardNumber()).orElseThrow(() -> new CardNumberNotFoundException(registrationRequest.getCardNumber()));
        return HashingUtil.compareRawAndHashedData(registrationRequest.getPeselNumber(), cardNumberEntity.getPeselNumber());
    }

    private void sendAccountEnabledMail(ConfirmationTokenEntity confirmationTokenEntity) {
        mailSender.send(confirmationTokenEntity.getUserEntity().getUsername(),
                mailBuilder.getAccountEnabledMailBody(
                        confirmationTokenEntity.getUserEntity().getFirstName(),
                        confirmationTokenEntity.getUserEntity().getLastName(),
                        loginPageAddress), "Account enabled");
        log.info("New sendAccountEnabledMail message sent to " + confirmationTokenEntity.getUserEntity().getUsername());
    }

    private void sendAccountConfirmationMail(RegistrationRequest registrationRequest, String registrationTime
            , String confirmationAddress) {
        mailSender.send(adminMail, mailBuilder.getAccountConfirmationMailBody(
                        registrationRequest.getUsername(),
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getCardNumber().toString(),
                        registrationTime,
                        confirmationAddress),
                "New account registration request");
        log.info("New sendAccountConfirmationMail message sent to " + adminMail);
    }

    private void sendAccountRegistrationMail(RegistrationRequest registrationRequest) {
        mailSender.send(registrationRequest.getUsername(), mailBuilder.getAccountRegisteredMailBody(
                        registrationRequest.getUsername(),
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getCardNumber().toString()),
                "Your account has been created");
        log.info("New sendAccountRegistrationMail message sent to " + registrationRequest.getUsername());
    }

}