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
    public List<UserEntity> getUsers() {
        return usersService.getUsers();
    }

    @GetMapping
    @RequestMapping(params = "userId")
    public List<UserEntity> getUserById(@RequestParam(required = false) Long userId) {
        return usersService.getUserById(userId);
    }

    @GetMapping
    @RequestMapping(params = "username")
    public List<UserEntity> getUserByUsername(@RequestParam(required = false) String username) {
        return usersService.getUserByUsername(username);
    }

    @GetMapping
    @RequestMapping(params = "cardNumber")
    public List<UserEntity> getUserByCardNumber(@RequestParam(required = false) Long cardNumber) {
        return usersService.getUserByCardNumber(cardNumber);
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
