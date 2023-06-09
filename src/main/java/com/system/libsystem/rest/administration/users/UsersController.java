package com.system.libsystem.rest.administration.users;

import com.system.libsystem.entities.user.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
