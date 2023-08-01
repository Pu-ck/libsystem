package com.system.libsystem.rest.books.favourite;

import javax.servlet.http.HttpServletRequest;

public interface FavouriteBookService {

    void addBookToFavourites(HttpServletRequest httpServletRequest, FavouriteBookRequest favouriteBookRequest);

    void removeBookFromFavourites(HttpServletRequest httpServletRequest, Long bookId);

}
