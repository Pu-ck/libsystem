package com.system.libsystem.rest.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/home/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        return userProfileService.getUserProfileInformation(httpServletRequest);
    }

    @PostMapping("/change-password")
    public void changeUserPassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        userProfileService.changeUserPassword(request, httpServletRequest);
    }

}
