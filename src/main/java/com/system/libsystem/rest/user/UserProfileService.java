package com.system.libsystem.rest.user;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.helpermodels.UserBook;
import com.system.libsystem.session.SessionRegistry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.system.libsystem.util.SharedConstants.USER_EXCEPTION_LOG;

@Service
@AllArgsConstructor
public class UserProfileService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;

    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG + username));

        return List.of(userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getCardNumber().toString());
    }

    public List<UserBook> getBooksBorrowedByUser(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG + username));
        final int userId = userEntity.getId();

        List<UserBook> userBooks = new ArrayList<>();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookRepository.findByUserId(userId)) {
            final BookEntity bookEntity = bookRepository.findById(borrowedBookEntity.getBookId())
                    .orElseThrow(() -> new IllegalStateException("Unable to find book with id: "
                            + borrowedBookEntity.getBookId()));

            UserBook userBook = new UserBook();
            userBook.setTitle(bookEntity.getTitle());
            userBook.setAuthor(bookEntity.getAuthor());
            userBook.setGenre(bookEntity.getGenre());
            userBook.setPublisher(bookEntity.getPublisher());
            userBook.setYearOfPrint(bookEntity.getYearOfPrint());
            userBook.setBorrowDate(borrowedBookEntity.getBorrowDate().toString());
            userBook.setReturnDate(borrowedBookEntity.getReturnDate().toString());
            userBook.setPenalty(borrowedBookEntity.getPenalty().toString());
            userBooks.add(userBook);
        }

        return userBooks;
    }

    public void changeUserPassword(ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXCEPTION_LOG + username));

        final String oldPassword = userEntity.getPassword();
        final String newPassword = request.getNewPassword();
        final String requestOldPassword = request.getOldPassword();

        if (bCryptPasswordEncoder.matches(requestOldPassword, oldPassword)) {
            if (bCryptPasswordEncoder.matches(newPassword, oldPassword)) {
                throw new IllegalStateException("The new password is the same as old one");
            } else {
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                userEntity.setPassword(encodedPassword);
                userRepository.save(userEntity);
            }
        } else {
            throw new IllegalStateException("The old password is not correct");
        }
    }

}
