package com.system.libsystem.rest.administration.users;

import com.system.libsystem.entities.user.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UsersService {

    List<UserEntity> getUsers();

    List<UserEntity> getUserById(Long userId);

    List<UserEntity> getUserByUsername(String username);

    List<UserEntity> getUserByCardNumber(Long cardNumber);

    void updateUserEnabledStatus(UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest,
                                 HttpServletRequest httpServletRequest);

    Long getAdminId(HttpServletRequest httpServletRequest);

}
