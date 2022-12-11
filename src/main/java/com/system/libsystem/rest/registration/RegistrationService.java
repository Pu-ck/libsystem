package com.system.libsystem.rest.registration;

import com.system.libsystem.user.UserEntity;
import com.system.libsystem.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;

    public String register(RegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(request.getPassword());
        userEntity.setUsername(request.getUsername());
        userEntity.setCardNumber(request.getCardNumber());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        return userService.registerUser(userEntity);
    }
}
