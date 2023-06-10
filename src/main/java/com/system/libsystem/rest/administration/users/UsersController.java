package com.system.libsystem.rest.administration.users;

import com.system.libsystem.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/users")
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public List<UserEntity> filterUsers(@RequestParam(required = false) Long userId) {
        return usersService.filterUsers(userId);
    }

    @PutMapping("/{user_id}/update-user-enabled-status")
    public void updateUserEnabledStatus(@RequestBody UpdateUserEnabledStatusRequest updateUserEnabledStatusRequest,
                                        @PathVariable("user_id") Long userId) {
        updateUserEnabledStatusRequest.setUserId(userId);
        usersService.updateUserEnabledStatus(updateUserEnabledStatusRequest);
    }

}
