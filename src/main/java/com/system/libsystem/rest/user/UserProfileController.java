package com.system.libsystem.rest.user;

import com.system.libsystem.helpermodels.UserBook;
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

    @GetMapping("/borrowed-books")
    public List<UserBook> getBooksBorrowedByUser(HttpServletRequest httpServletRequest) {
        return userProfileService.getBooksBorrowedByUser(httpServletRequest);
    }

    @GetMapping("/ordered-books")
    public List<UserBook> getBooksOrderedByUser(HttpServletRequest httpServletRequest) {
        return userProfileService.getBooksOrderedByUser(httpServletRequest);
    }

    @PostMapping("/change-password")
    public void changeUserPassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        userProfileService.changeUserPassword(request, httpServletRequest);
    }

}
