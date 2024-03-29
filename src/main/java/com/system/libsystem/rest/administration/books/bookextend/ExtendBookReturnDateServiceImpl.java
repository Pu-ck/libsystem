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

import java.sql.Date;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ExtendBookReturnDateServiceImpl implements ExtendBookReturnDateService {

    private static final int DEFAULT_BORROW_TIME = 31;

    private final BorrowedBookRepository borrowedBookRepository;
    private final UserService userService;
    private final BookService bookService;
    private final BorrowedBookService borrowedBookService;
    private final MailSender mailSender;

    @Override
    public void extendRequestedBookReturnDate(ExtendBookReturnDateRequest extendBookReturnDateRequest) {
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
        if (!isBorrowedBookFeasibleForExtension(borrowedBookEntity)) {
            throw new BookAlreadyExtendedException(borrowedBookEntity.getId());
        }
        saveBorrowedBookNewReturnDate(borrowedBookEntity, newReturnDate, userEntity, bookEntity);
    }

    private boolean isBorrowedBookFeasibleForExtension(BorrowedBookEntity borrowedBookEntity) {
        final Date borrowDate = borrowedBookEntity.getBorrowDate();
        final Date returnDate = borrowedBookEntity.getReturnDate();
        final long days = TimeUnit.DAYS.convert(returnDate.getTime() - borrowDate.getTime(), TimeUnit.MILLISECONDS);
        return borrowedBookEntity.isExtended() && days <= DEFAULT_BORROW_TIME;
    }

    private void saveBorrowedBookNewReturnDate(BorrowedBookEntity borrowedBookEntity, Date newReturnDate,
                                               UserEntity userEntity, BookEntity bookEntity) {
        borrowedBookEntity.setExtended(true);
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
