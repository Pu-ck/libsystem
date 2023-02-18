package com.system.libsystem.rest.user;

import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@AllArgsConstructor
public class UserProfileService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;


    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        UserEntity userEntity = userRepository.findByUsername(username);
        return List.of(userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getCardNumber().toString());
    }

    public void changeUserPassword(ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        UserEntity userEntity = userRepository.findByUsername(username);

        final String oldPassword = userEntity.getPassword();
        final String newPassword = request.getNewPassword();
        final String requestOldPassword = request.getOldPassword();

        if (bCryptPasswordEncoder.matches(requestOldPassword, oldPassword)) {
            if (bCryptPasswordEncoder.matches(newPassword, oldPassword)) {
                throw new IllegalStateException("The new password is the same as old one");
            } else {
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                userEntity.setPassword(encodedPassword);
                userRepository.save(userEntity);
            }
        } else {
            throw new IllegalStateException("The old password is not correct");
        }
    }

}
