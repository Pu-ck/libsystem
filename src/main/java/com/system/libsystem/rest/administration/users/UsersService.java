package com.system.libsystem.rest.administration.users;

import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.rest.administration.administration.AdministratorChangesItsOwnEnabledStatusException;
import com.system.libsystem.exceptions.user.UserNotFoundException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final UserService userService;

    @Value("${application.login.address}")
    private String loginPageAddress;

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public List<UserEntity> getUserById(Long userId) {
        if (userId == null) {
            return getUsers();
        }
        final List<UserEntity> userEntities = new ArrayList<>();
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId, null, null));
        userEntities.add(userEntity);
        return userEntities;
    }

    public List<UserEntity> getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return getUsers();
        }
        final List<UserEntity> userEntities = new ArrayList<>();
        final UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(null, null, username));
        userEntities.add(userEntity);
        return userEntities;
    }

    public List<UserEntity> getUserByCardNumber(Long cardNumber) {
        if (cardNumber == null) {
            return getUsers();
        }
        final List<UserEntity> userEntities = new ArrayList<>();
        final UserEntity userEntity = userRepository.findByCardNumber(cardNumber).orElseThrow(() ->
                new UserNotFoundException(null, cardNumber, null));
        userEntities.add(userEntity);
        return userEntities;
    }

    public void updateUserEnabledStatus(UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest,
                                        HttpServletRequest httpServletRequest) {
        final UserEntity userEntity = userRepository.findById(updateUserEnabledStatusRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(updateUserEnabledStatusRequest.getUserId(), null, null));
        final UserEntity admin = userService.getCurrentlyLoggedUser(httpServletRequest);

        if (Objects.equals(admin.getId(), userEntity.getId())) {
            throw new AdministratorChangesItsOwnEnabledStatusException();
        }
        if (userEntity.isEnabled()) {
            disableUser(userEntity, updateUserEnabledStatusRequest);
        } else {
            enableUser(userEntity);
        }
    }

    public Long getAdminId(HttpServletRequest httpServletRequest) {
        return userService.getCurrentlyLoggedUser(httpServletRequest).getId();
    }

    private void disableUser(UserEntity userEntity, UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest) {
        userEntity.setEnabled(false);
        userRepository.save(userEntity);
        log.info("User with id " + userEntity.getId() + " has been disabled");
        sendAccountDisabledMail(userEntity, updateUserEnabledStatusRequest.getReason());
    }

    private void enableUser(UserEntity userEntity) {
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        log.info("User with id " + userEntity.getId() + " has been enabled");
        sendAccountEnabledMail(userEntity);
    }

    private void sendAccountDisabledMail(UserEntity userEntity, String reason) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getAccountDisabledMailBody(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                reason), "Account disabled");
        log.info("New sendAccountDisabledMail message sent to " + userEntity.getUsername());
    }

    private void sendAccountEnabledMail(UserEntity userEntity) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getAccountEnabledMailBody(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                loginPageAddress), "Account enabled");
        log.info("New sendAccountEnabledMail message sent to " + userEntity.getUsername());
    }

}
