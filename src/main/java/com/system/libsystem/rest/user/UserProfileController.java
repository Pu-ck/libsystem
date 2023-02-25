package com.system.libsystem.rest.user;

import com.system.libsystem.helpermodels.UserBook;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        return userProfileService.getUserProfileInformation(httpServletRequest);
    }

    @PostMapping("/borrowed-books")
    public void extendBookReturnDate(@RequestBody ExtendBookRequest extendBookRequest,
                                     HttpServletRequest httpServletRequest) {
        userProfileService.extendBookReturnDate(extendBookRequest, httpServletRequest);
    }

    @GetMapping("/borrowed-books")
    public List<UserBook> getBooksBorrowedByUser(HttpServletRequest httpServletRequest) {
        return userProfileService.getBooksBorrowedByUser(httpServletRequest);
    }

    @PostMapping("/change-password")
    public void changeUserPassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        userProfileService.changeUserPassword(request, httpServletRequest);
    }

}
