package com.system.libsystem.entities.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {

    UserEntity loadUserByUsername(String username) throws UsernameNotFoundException;

    UserEntity getUserById(Long userId) throws UsernameNotFoundException;

    UserEntity getUserByUsername(String username) throws UsernameNotFoundException;

    String registerUser(UserEntity userEntity);

    void enableUser(String username);

    void setOrUpdateUserPassword(UserEntity userEntity, String newPassword);

    void validateIfUserIsEnabledByServletRequest(HttpServletRequest httpServletRequest);

    UserEntity getCurrentlyLoggedUser(HttpServletRequest httpServletRequest);

}
