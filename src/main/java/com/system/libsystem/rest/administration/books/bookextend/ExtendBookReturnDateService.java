package com.system.libsystem.rest.administration.books.bookextend;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

import static com.system.libsystem.util.SharedConstants.BOOK_ALREADY_EXTENDED_LOG;
import static com.system.libsystem.util.SharedConstants.BOOK_ALREADY_RETURNED_LOG;

@Service
@AllArgsConstructor
@Slf4j
public class ExtendBookReturnDateService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final UserService userService;
    private final BookService bookService;
    private final BorrowedBookService borrowedBookService;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;

    public void extendRequestedBookReturnDate(ExtendBookReturnDateRequest extendBookReturnDateRequest) {

        BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById(extendBookReturnDateRequest
                .getBorrowedBookId());
        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
        final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

        final Date currentReturnDate = borrowedBookEntity.getReturnDate();
        final LocalDate extendTime = currentReturnDate.toLocalDate().plusDays(extendBookReturnDateRequest.getExtendTime());
        final Date newReturnDate = Date.valueOf(extendTime);

        if (!borrowedBookEntity.isClosed()) {
            if (borrowedBookEntity.isAccepted()) {
                if (!borrowedBookEntity.isExtended()) {
                    saveBorrowedBookNewReturnDate(borrowedBookEntity, newReturnDate, userEntity, bookEntity);
                } else {
                    throw new IllegalStateException(BOOK_ALREADY_EXTENDED_LOG + borrowedBookEntity.getId());
                }
            } else {
                throw new IllegalStateException("Unable to extended return date of the requested borrowed book with id "
                        + borrowedBookEntity.getId() + " because the book is not accepted");
            }
        } else {
            throw new IllegalStateException(BOOK_ALREADY_RETURNED_LOG + borrowedBookEntity.getId());
        }
    }

    private void saveBorrowedBookNewReturnDate(BorrowedBookEntity borrowedBookEntity, Date newReturnDate,
                                               UserEntity userEntity, BookEntity bookEntity) {
        borrowedBookEntity.setReturnDate(newReturnDate);
        borrowedBookEntity.setExtended(true);
        borrowedBookRepository.save(borrowedBookEntity);
        sendBookReturnDateExtensionConfirmationMail(userEntity, borrowedBookEntity, bookEntity);
        log.info("New return date set for borrowed book with id " + borrowedBookEntity.getId());
    }

    private void sendBookReturnDateExtensionConfirmationMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity,
                                                             BookEntity bookEntity) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getBookReturnDateExtensionConfirmationMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getAuthor(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliate()),
                "Book return date extension request accepted");
        log.info("New sendBookReturnDateExtensionConfirmationMail sent to " + userEntity.getUsername());
    }

}
