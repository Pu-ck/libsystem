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
import com.system.libsystem.scheduler.SchedulerDateUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPickUpCheckerTask {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final BookService bookService;
    private final MailSender mailSender;
    private final BookUtil bookUtil;

    @Transactional
    public void checkIfOrderedBooksWerePickedUp() {
        final Date currentDate = SchedulerDateUtil.getCurrentDate();
        final List<BorrowedBookEntity> borrowedBookEntities = borrowedBookRepository.findAll();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookEntities) {
            if (borrowedBookEntity.isReady() && !borrowedBookEntity.isClosed()) {
                final Date borrowedBookReadyDate = borrowedBookEntity.getReadyDate();
                final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
                final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

                if (isOrderedBookPickUpTimeOverdue(currentDate, borrowedBookReadyDate)) {
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
        log.info("Closing borrowed book order with id " + borrowedBookEntity.getId() + " due to pickup time overdue");
        sendBorrowedBookOrderPickUpTimeExpiredMail(userEntity, bookEntity, borrowedBookEntity.getAffiliateEntity().getName());
    }

    private boolean isOrderedBookPickUpTimeOverdue(Date currentDate, Date borrowedBookReadyDate) {
        return currentDate.compareTo(borrowedBookReadyDate) > 0;
    }

    private void sendBorrowedBookOrderPickUpTimeExpiredMail(UserEntity userEntity, BookEntity bookEntity, String affiliate) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getBorrowedBookOrderPickUpTimeExpiredMailBody
                (userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getFormattedAuthorsAsString(),
                        affiliate), "Ordered book pick up time expired");
        log.info("New sendBorrowedBookOrderPickUpTimeExpired message sent to " + userEntity.getUsername());
    }

}
