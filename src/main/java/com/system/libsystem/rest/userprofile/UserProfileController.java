package com.system.libsystem.rest.userprofile;

import com.system.libsystem.helpermodels.UserBook;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        return userProfileService.getUserProfileInformation(httpServletRequest);
    }

    @GetMapping("/books")
    public List<UserBook> getUserBooks(HttpServletRequest httpServletRequest) {
        return userProfileService.getUserBooks(httpServletRequest);
    }

    @GetMapping("/favourites")
    public Set<FavouriteBookDTO> getUserFavouriteBooks(HttpServletRequest httpServletRequest) {
        return userProfileService.getUserFavouriteBooks(httpServletRequest);
    }

    @PutMapping("/books/{book_id}/extend-book")
    public void extendBookReturnDate(@RequestBody ExtendBookRequest extendBookRequest,
                                     @PathVariable("book_id") Long bookId,
                                     HttpServletRequest httpServletRequest) {
        extendBookRequest.setBorrowedBookId(bookId);
        userProfileService.extendBookReturnDate(extendBookRequest, httpServletRequest);
    }

    @PutMapping("/change-password")
    public void changeUserPassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        userProfileService.changeUserPassword(request, httpServletRequest);
    }

}
