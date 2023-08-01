package com.system.libsystem.rest.books.favourite;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.book.BookNotFoundException;
import com.system.libsystem.exceptions.favourite.BookNotFoundInUserFavouriteBooksException;
import com.system.libsystem.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavouriteBookServiceImpl implements FavouriteBookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final SessionRegistry sessionRegistry;
    private final UserService userService;

    @Transactional
    @Override
    public void addBookToFavourites(HttpServletRequest httpServletRequest, FavouriteBookRequest favouriteBookRequest) {
        final UserEntity userEntity = userService.getCurrentlyLoggedUser(httpServletRequest);
        final BookEntity bookEntity = bookRepository.findById(favouriteBookRequest.getBookId()).orElseThrow(() ->
                new BookNotFoundException(favouriteBookRequest.getBookId()));
        final Set<BookEntity> updatedFavouriteBooks = userEntity.getFavouriteBooks();
        updatedFavouriteBooks.add(bookEntity);
        updateUserFavouriteBooksAndSaveInRepository(userEntity, updatedFavouriteBooks);
    }

    @Transactional
    @Override
    public void removeBookFromFavourites(HttpServletRequest httpServletRequest, Long bookId) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userService.getUserByUsername(username);
        final BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        if (!userEntity.getFavouriteBooks().contains(bookEntity)) {
            throw new BookNotFoundInUserFavouriteBooksException(userEntity.getId());
        }

        final Set<BookEntity> updatedFavouriteBooks = userEntity.getFavouriteBooks();
        updatedFavouriteBooks.remove(bookEntity);
        updateUserFavouriteBooksAndSaveInRepository(userEntity, updatedFavouriteBooks);
    }

    private void updateUserFavouriteBooksAndSaveInRepository(UserEntity userEntity, Set<BookEntity> updatedFavouriteBooks) {
        userEntity.setFavouriteBooks(updatedFavouriteBooks);
        userRepository.save(userEntity);
        log.info("Favourite books of user " + userEntity.getUsername() + " have been updated");
    }

}
