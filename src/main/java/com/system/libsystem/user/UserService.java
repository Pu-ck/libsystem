package com.system.libsystem.user;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        final UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            final User user = new User();
            user.setUsername(userEntity.getUsername());
            user.setPassword(userEntity.getPassword());
            return user;
        }
        else {
            throw new UsernameNotFoundException("Unable to find user " + username);
        }
    }
}
