package com.system.libsystem.rest.administration.books.bookextend;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.book.BookAlreadyExtendedException;
import com.system.libsystem.exceptions.book.BookAlreadyReturnedException;
import com.system.libsystem.exceptions.book.UnableToExtendNotAcceptedBookException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class ExtendBookReturnDateService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final UserService userService;
    private final BookService bookService;
    private final BorrowedBookService borrowedBookService;
    private final MailSender mailSender;

    @Transactional
    public void extendRequestedBookReturnDate(ExtendBookReturnDateRequest extendBookReturnDateRequest,
                                              HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        final BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(extendBookReturnDateRequest
                .getBorrowedBookId());
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
        final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

        final Date currentReturnDate = borrowedBookEntity.getReturnDate();
        final LocalDate extendTime = currentReturnDate.toLocalDate().plusDays(extendBookReturnDateRequest.getExtendTime());
        final Date newReturnDate = Date.valueOf(extendTime);

        if (borrowedBookEntity.isClosed()) {
            throw new BookAlreadyReturnedException(borrowedBookEntity.getId());
        }
        if (!borrowedBookEntity.isAccepted()) {
            throw new UnableToExtendNotAcceptedBookException(borrowedBookEntity.getId());
        }
        if (borrowedBookEntity.isExtended()) {
            throw new BookAlreadyExtendedException(borrowedBookEntity.getId());
        }
        saveBorrowedBookNewReturnDate(borrowedBookEntity, newReturnDate, userEntity, bookEntity);
    }

    private void saveBorrowedBookNewReturnDate(BorrowedBookEntity borrowedBookEntity, Date newReturnDate,
                                               UserEntity userEntity, BookEntity bookEntity) {
        borrowedBookEntity.setReturnDate(newReturnDate);
        borrowedBookRepository.save(borrowedBookEntity);
        sendBookReturnDateExtensionConfirmationMail(userEntity, borrowedBookEntity, bookEntity);
        log.info("New return date set for borrowed book with id " + borrowedBookEntity.getId());
    }

    private void sendBookReturnDateExtensionConfirmationMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity,
                                                             BookEntity bookEntity) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getBookReturnDateExtensionConfirmationMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getFormattedAuthorsAsString(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliateEntity().getName()),
                "Book return date extension request accepted");
        log.info("New sendBookReturnDateExtensionConfirmationMail sent to " + userEntity.getUsername());
    }

}
