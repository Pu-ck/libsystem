package com.system.libsystem.rest.administration.users;

import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.exceptions.user.UserNotFoundException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UsersService {

    private final UserRepository userRepository;
    private final MailSender mailSender;

    @Value("${application.login.address}")
    private String loginPageAddress;

    public List<UserEntity> filterUsers(Long userId) {
        if (userId == null) {
            return userRepository.findAll();
        }
        return getUserById(userId);
    }

    public void updateUserEnabledStatus(UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest) {
        final UserEntity userEntity = userRepository.findById(updateUserEnabledStatusRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(updateUserEnabledStatusRequest.getUserId()));
        if (userEntity.isEnabled()) {
            disabledUser(userEntity, updateUserEnabledStatusRequest);
        } else {
            enableUser(userEntity);
        }
    }

    private void disabledUser(UserEntity userEntity, UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest) {
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

    private List<UserEntity> getUserById(Long userId) {
        final List<UserEntity> userEntities = new ArrayList<>();
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
        userEntities.add(userEntity);
        return userEntities;
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
