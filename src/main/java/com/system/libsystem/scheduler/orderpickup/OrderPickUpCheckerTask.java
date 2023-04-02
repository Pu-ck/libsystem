package com.system.libsystem.scheduler.orderpickup;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.rest.util.BookUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OrderPickUpCheckerTask {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final BookService bookService;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;
    private final BookUtil bookUtil;

    @Transactional
    public void checkIfOrderedBooksWerePickedUp() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final Date currentDate = calendar.getTime();
        List<BorrowedBookEntity> borrowedBookEntities = borrowedBookRepository.findAll();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookEntities) {
            if (borrowedBookEntity.isReady() && !borrowedBookEntity.isClosed()) {
                final Date borrowedBookReadyDate = borrowedBookEntity.getReadyDate();
                final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
                final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

                if (isOrderedBookPickUpOverdue(currentDate, borrowedBookReadyDate)) {
                    setBorrowedBookOrderAsClosed(bookEntity, borrowedBookEntity, userEntity);
                }
            }
        }
    }

    private void setBorrowedBookOrderAsClosed(BookEntity bookEntity, BorrowedBookEntity borrowedBookEntity,
                                              UserEntity userEntity) {
        borrowedBookEntity.setClosed(true);
        bookUtil.setCurrentQuantityInAffiliate(bookEntity, borrowedBookEntity.getAffiliateEntity().getName(), 1);
        bookRepository.save(bookEntity);
        borrowedBookRepository.save(borrowedBookEntity);
        sendBorrowedBookOrderPickUpTimeExpired(userEntity, bookEntity, borrowedBookEntity.getAffiliateEntity().getName());
    }

    private boolean isOrderedBookPickUpOverdue(Date currentDate, Date borrowedBookReadyDate) {
        return currentDate.compareTo(borrowedBookReadyDate) > 0;
    }

    private void sendBorrowedBookOrderPickUpTimeExpired(UserEntity userEntity, BookEntity bookEntity, String affiliate) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getBorrowedBookOrderPickUpTimeExpired
                (userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        String.join(",", bookEntity.getAuthors().stream().toList().toString()),
                        affiliate), "Ordered book pick up time expired");
        log.info("New sendBorrowedBookOrderPickUpTimeExpired message sent to " + userEntity.getUsername());
    }

}
