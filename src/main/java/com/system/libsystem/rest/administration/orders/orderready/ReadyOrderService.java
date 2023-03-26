package com.system.libsystem.rest.administration.orders.orderready;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.borrowedbook.BorrowedBookService;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.BookAlreadyReturnedException;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadyOrderService {

    private static final int DAYS_TO_PICK_UP_ORDERED_BOOK = 2;

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final BookUtil bookUtil;
    private final BorrowedBookService borrowedBookService;
    private final UserService userService;
    private final BookService bookService;
    private final MailSender mailSender;
    private final MailBuilder mailBuilder;

    @Transactional
    public void setOrderStatus(ReadyOrderRequest readyOrderRequest) {

        final Date currentDate = new Date(System.currentTimeMillis());
        final Date readyDate = Date.valueOf(currentDate.toLocalDate().plusDays(DAYS_TO_PICK_UP_ORDERED_BOOK));

        BorrowedBookEntity borrowedBookEntity = borrowedBookService.getBorrowedBookById
                (readyOrderRequest.getBorrowedBookId());

        final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
        final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

        if (!borrowedBookEntity.isClosed()) {
            if (readyOrderRequest.isAccepted()) {
                setOrderAsReady(borrowedBookEntity, userEntity, bookEntity, readyDate);
            } else {
                setOrderAsRejected(borrowedBookEntity, userEntity, bookEntity);
            }
        } else {
            throw new BookAlreadyReturnedException(borrowedBookEntity.getId());
        }
    }

    private void setOrderAsReady(BorrowedBookEntity borrowedBookEntity, UserEntity userEntity, BookEntity bookEntity,
                                 Date readyDate) {
        borrowedBookEntity.setReadyDate(readyDate);
        borrowedBookEntity.setReady(true);
        borrowedBookRepository.save(borrowedBookEntity);
        sendBorrowedBookOrderReadyMail(userEntity, bookEntity, borrowedBookEntity.getAffiliate());
    }

    private void setOrderAsRejected(BorrowedBookEntity borrowedBookEntity, UserEntity userEntity,
                                    BookEntity bookEntity) {
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, borrowedBookEntity.getAffiliate(), 1);
        bookRepository.save(bookEntity);
        borrowedBookEntity.setClosed(true);
        borrowedBookRepository.save(borrowedBookEntity);
        sendBorrowedBookOrderRejected(userEntity, bookEntity, borrowedBookEntity.getAffiliate());
    }

    private void sendBorrowedBookOrderReadyMail(UserEntity userEntity, BookEntity bookEntity, String affiliate) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getBorrowedBookOrderReadyMailBody
                (userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        String.join(",", bookEntity.getAuthors().stream().toList().toString()),
                        affiliate), "Ordered book ready");
        log.info("New sendBorrowedBookOrderReadyMail message sent to " + userEntity.getUsername());
    }

    private void sendBorrowedBookOrderRejected(UserEntity userEntity, BookEntity bookEntity, String affiliate) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getBorrowedBookOrderRejectedMailBody
                (userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        String.join(",", bookEntity.getAuthors().stream().toList().toString()),
                        affiliate), "Ordered book rejected");
        log.info("New sendBorrowedBookOrderRejected message sent to " + userEntity.getUsername());
    }

}
