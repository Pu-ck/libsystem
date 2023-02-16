package com.system.libsystem.rest.user;

import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@AllArgsConstructor
public class UserProfileService {

    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;

    public List<String> getUserProfileInformation(HttpServletRequest request) {
        final String sessionID = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        UserEntity userEntity = userRepository.findByUsername(username);
        return List.of(userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName());
    }

}
