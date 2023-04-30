package com.system.libsystem.rest.user;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.BookAlreadyExtendedException;
import com.system.libsystem.exceptions.BookAlreadyReturnedException;
import com.system.libsystem.exceptions.InvalidCardNumberException;
import com.system.libsystem.exceptions.NewPasswordDuplicatedException;
import com.system.libsystem.helpermodels.UserBook;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import com.system.libsystem.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;
    private final BorrowedBookService borrowedBookService;
    private final UserService userService;
    private final BookService bookService;
    private final BookUtil bookUtil;

    @Value("${mail.sender.admin}")
    private String adminMail;

    public List<String> getUserProfileInformation(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userService.getUserByUsername(username);

        return List.of(userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getCardNumber().toString());
    }

    public List<UserBook> getUserBooks(HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);
        final UserEntity userEntity = userService.getUserByUsername(username);
        final int userId = userEntity.getId();

        List<UserBook> userBooks = new ArrayList<>();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookRepository.findByUserId(userId)) {
            final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());
            UserBook userBook = new UserBook();
            setUserBookDetails(userBook, borrowedBookEntity, bookEntity);

            if (borrowedBookEntity.isClosed()) {
                setReturnedOrRejectedBookDetails(userBook, borrowedBookEntity);
            } else if (borrowedBookEntity.isAccepted()) {
                setBorrowedBookDetails(userBook, borrowedBookEntity);
            } else if (borrowedBookEntity.isReady()) {
                setReadyBookDetails(userBook, borrowedBookEntity);
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

        UserEntity userEntity = userService.getUserByUsername(username);

        final String oldPassword = userEntity.getPassword();
        final String newPassword = request.getNewPassword();
        final String requestOldPassword = request.getOldPassword();

        if (isRequestOldPasswordMatchingOldPassword(requestOldPassword, oldPassword)) {
            if (isNewPasswordSameAsOldPassword(newPassword, oldPassword)) {
                log.error("The new password set by user " + username + " with id " + userEntity.getId()
                        + " is the same as old one");
                throw new NewPasswordDuplicatedException();
            } else {
                saveNewPassword(userEntity, newPassword);
            }
        } else {
            throw new IllegalStateException("The old password is not correct");
        }
    }

    public void extendBookReturnDate(ExtendBookRequest extendBookRequest, HttpServletRequest httpServletRequest) {
        final String sessionID = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String username = sessionRegistry.getSessionUsername(sessionID);

        final UserEntity userEntity = userService.getUserByUsername(username);
        final BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(extendBookRequest
                .getBorrowedBookId());

        if (bookUtil.isCardNumberValid(userEntity.getCardNumber(), extendBookRequest.getCardNumber())) {
            if (!borrowedBookEntity.isClosed()) {
                if (!borrowedBookEntity.isExtended()) {
                    sendBookReturnDateExtensionRequestMail(userEntity, borrowedBookEntity);
                    log.info("New request for return date extension of borrowed book with id "
                            + borrowedBookEntity.getId() + " has been issued by user with id " + userEntity.getId());
                } else {
                    throw new BookAlreadyExtendedException(borrowedBookEntity.getId());
                }
            } else {
                throw new BookAlreadyReturnedException(borrowedBookEntity.getId());
            }
        } else {
            throw new InvalidCardNumberException();
        }
    }

    private void saveNewPassword(UserEntity userEntity, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
        log.info("New password for user with id " + userEntity.getId() + " has been set (via user profile)");
    }

    private void setUserBookDetails(UserBook userBook, BorrowedBookEntity borrowedBookEntity, BookEntity bookEntity) {
        userBook.setTitle(bookEntity.getTitle());
        userBook.setAuthors(bookEntity.getAuthors());
        userBook.setGenres(bookEntity.getGenres());
        userBook.setPublisherName(bookEntity.getPublisherEntity().getName());
        userBook.setYearOfPrint(Integer.toString(bookEntity.getYearOfPrintEntity().getYearOfPrint()));
        userBook.setAffiliate(borrowedBookEntity.getAffiliateEntity().getName());
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
        userBook.setReadyDate("");
        userBook.setPenalty("");
        userBook.setStatus("Ordered");
    }

    private void setReadyBookDetails(UserBook userBook, BorrowedBookEntity borrowedBookEntity) {
        userBook.setBorrowDate("");
        userBook.setReturnDate("");
        userBook.setReadyDate(borrowedBookEntity.getReadyDate().toString());
        userBook.setPenalty("");
        userBook.setStatus("Ready");
    }

    private void setReturnedOrRejectedBookDetails(UserBook userBook, BorrowedBookEntity borrowedBookEntity) {
        if (borrowedBookEntity.getBorrowDate() != null && borrowedBookEntity.getReturnDate() != null
                && borrowedBookEntity.getPenalty() != null) {
            userBook.setBorrowDate(borrowedBookEntity.getBorrowDate().toString());
            userBook.setReturnDate(borrowedBookEntity.getReturnDate().toString());
            userBook.setPenalty(borrowedBookEntity.getPenalty().toString());
            userBook.setStatus("Returned");
        } else {
            userBook.setBorrowDate("");
            userBook.setReturnDate("");
            userBook.setReadyDate("");
            userBook.setPenalty("");
            userBook.setStatus("Rejected");
        }
    }

    private boolean isRequestOldPasswordMatchingOldPassword(String requestOldPassword, String oldPassword) {
        return bCryptPasswordEncoder.matches(requestOldPassword, oldPassword);
    }

    private boolean isNewPasswordSameAsOldPassword(String newPassword, String oldPassword) {
        return bCryptPasswordEncoder.matches(newPassword, oldPassword);
    }

    private void sendBookReturnDateExtensionRequestMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity) {
        mailSender.send(adminMail, mailBuilder.getBookReturnDateExtensionRequestMailBody(
                        userEntity.getId(),
                        userEntity.getCardNumber().toString(),
                        borrowedBookEntity.getId(),
                        borrowedBookEntity.getBorrowDate().toString(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliateEntity().getName(),
                        borrowedBookEntity.getPenalty()),
                "New book return date extension request");
        log.info("New sendBookReturnDateExtensionRequestMail message sent to " + adminMail);
    }

}
