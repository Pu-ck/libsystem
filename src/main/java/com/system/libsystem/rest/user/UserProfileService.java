package com.system.libsystem.rest.user;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.helpermodels.UserBook;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.system.libsystem.util.SharedConstants.*;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;

    @Value("${mail.admin}")
    private String adminMail;

    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));

        return List.of(userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getCardNumber().toString());
    }

    public List<UserBook> getBooksBorrowedByUser(HttpServletRequest httpServletRequest) {

        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));
        final int userId = userEntity.getId();

        List<UserBook> userBooks = new ArrayList<>();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookRepository.findByUserId(userId)) {
            final BookEntity bookEntity = bookRepository.findById(borrowedBookEntity.getBookId())
                    .orElseThrow(() -> new IllegalStateException(FIND_BOOK_EXCEPTION_LOG
                            + borrowedBookEntity.getBookId()));
            UserBook userBook = new UserBook();
            userBook.setTitle(bookEntity.getTitle());
            userBook.setAuthor(bookEntity.getAuthor());
            userBook.setGenre(bookEntity.getGenre());
            userBook.setPublisher(bookEntity.getPublisher());
            userBook.setYearOfPrint(bookEntity.getYearOfPrint());
            userBook.setAffiliate(borrowedBookEntity.getAffiliate());
            if (borrowedBookEntity.isAccepted()) {
                setBorrowedBookDetails(userBook, borrowedBookEntity);
            } else {
                setOrderedBookDetails(userBook);
            }
            userBooks.add(userBook);
        }

        return userBooks;
    }

    public void changeUserPassword(ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));

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

    public void extendBookReturnDate(ExtendBookRequest extendBookRequest, HttpServletRequest httpServletRequest) {

        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);

        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(FIND_USER_EXCEPTION_LOG + username));

        final BorrowedBookEntity borrowedBookEntity = borrowedBookRepository
                .findById(extendBookRequest.getBorrowedBookId())
                .orElseThrow(() -> new IllegalStateException(FIND_BORROWED_BOOK_EXCEPTION_LOG));

        if (Objects.equals(userEntity.getCardNumber(), extendBookRequest.getCardNumber())) {
            if (!borrowedBookEntity.isExtended()) {
                sendBookReturnDateExtensionRequestMail(userEntity, borrowedBookEntity);
            } else {
                throw new IllegalStateException("The borrowed book with id " + borrowedBookEntity.getId() +
                        " has been already extended once");
            }
        } else {
            throw new IllegalStateException(INVALID_CARD_NUMBER_LOG);
        }

    }

    private void sendBookReturnDateExtensionRequestMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity) {
        mailSender.send(adminMail, mailBuilder.getBookReturnDateExtensionRequestMailBody(
                        userEntity.getId(),
                        userEntity.getCardNumber().toString(),
                        borrowedBookEntity.getBookId(),
                        borrowedBookEntity.getBorrowDate().toString(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliate(),
                        borrowedBookEntity.getPenalty()),
                "New book return date extension request");
    }


    private void setBorrowedBookDetails(UserBook userBook, BorrowedBookEntity borrowedBookEntity) {
        userBook.setBorrowDate(borrowedBookEntity.getBorrowDate().toString());
        userBook.setReturnDate(borrowedBookEntity.getReturnDate().toString());
        userBook.setPenalty(borrowedBookEntity.getPenalty().toString());
        userBook.setStatus("Borrowed");
    }

    private void setOrderedBookDetails(UserBook userBook) {
        userBook.setBorrowDate("");
        userBook.setReturnDate("");
        userBook.setPenalty("");
        userBook.setStatus("Ordered");
    }

}
