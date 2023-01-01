package com.system.libsystem.rest.registration;

import com.system.libsystem.rest.registration.token.ConfirmationToken;
import com.system.libsystem.rest.registration.token.ConfirmationTokenService;
import com.system.libsystem.user.UserEntity;
import com.system.libsystem.user.UserService;
import com.system.libsystem.user.UserType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(request.getPassword());
        userEntity.setUsername(request.getUsername());
        userEntity.setCardNumber(request.getCardNumber());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setEnabled(false);
        userEntity.setUserType(UserType.USER);
        return userService.registerUser(userEntity);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
                        new IllegalStateException("Confirmation token not found"));

        userService.enableUser(
                confirmationToken.getUserEntity().getUsername());

        return "Requested user was confirmed and enabled";
    }

}
