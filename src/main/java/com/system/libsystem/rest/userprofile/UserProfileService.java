package com.system.libsystem.rest.userprofile;

import com.system.libsystem.helpermodels.UserBook;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface UserProfileService {

    List<String> getUserProfileInformation(HttpServletRequest httpServletRequest);

    List<UserBook> getUserBooks(HttpServletRequest httpServletRequest);

    void changeUserPassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest httpServletRequest);

    void extendBookReturnDate(ExtendBookRequest extendBookRequest, HttpServletRequest httpServletRequest);

    Set<FavouriteBookDTO> getUserFavouriteBooks(HttpServletRequest httpServletRequest);

}
