package com.system.libsystem.rest.books.favourite;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class FavouriteBookController {

    private final FavouriteBookService favouriteBookService;

    @PostMapping("/{book_id}/add-to-favourites")
    public void addBookToFavourites(@RequestBody FavouriteBookRequest favouriteBookRequest,
                                    @PathVariable("book_id") Long bookId,
                                    HttpServletRequest httpServletRequest) {
        favouriteBookRequest.setBookId(bookId);
        favouriteBookService.addBookToFavourites(httpServletRequest, favouriteBookRequest);
    }

    @DeleteMapping("/{book_id}/remove-from-favourites")
    public void removeBookFromFavourites(@PathVariable("book_id") Long bookId,
                                         HttpServletRequest httpServletRequest) {
        favouriteBookService.removeBookFromFavourites(httpServletRequest, bookId);
    }

}
