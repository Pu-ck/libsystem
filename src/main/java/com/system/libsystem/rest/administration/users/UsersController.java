package com.system.libsystem.rest.administration.users;

import com.system.libsystem.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public List<UserEntity> getUsers(HttpServletRequest httpServletRequest) {
        return usersService.getUsers(httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "userId")
    public List<UserEntity> getUserById(@RequestParam(required = false) Long userId,
                                        HttpServletRequest httpServletRequest) {
        return usersService.getUserById(userId, httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "username")
    public List<UserEntity> getUserByUsername(@RequestParam(required = false) String username,
                                              HttpServletRequest httpServletRequest) {
        return usersService.getUserByUsername(username, httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "cardNumber")
    public List<UserEntity> getUserByCardNumber(@RequestParam(required = false) Long cardNumber,
                                                HttpServletRequest httpServletRequest) {
        return usersService.getUserByCardNumber(cardNumber, httpServletRequest);
    }

    @GetMapping("/admin-id")
    public Long getAdminId(HttpServletRequest httpServletRequest) {
        return usersService.getAdminId(httpServletRequest);
    }

    @PutMapping("/{user_id}/update-user-enabled-status")
    public void updateUserEnabledStatus(@RequestBody UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest,
                                        @PathVariable("user_id") Long userId,
                                        HttpServletRequest httpServletRequest) {
        updateUserEnabledStatusRequest.setUserId(userId);
        usersService.updateUserEnabledStatus(updateUserEnabledStatusRequest, httpServletRequest);
    }

}
