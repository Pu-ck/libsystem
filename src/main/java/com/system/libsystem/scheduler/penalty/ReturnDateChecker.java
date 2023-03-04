package com.system.libsystem.scheduler.penalty;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class ReturnDateChecker {

    private static final BigDecimal PENALTY = new BigDecimal("1.00");
    private static final int INCOMING_RETURN_DATE_REMINDER_PERIOD = 7;

    private final BorrowedBookRepository borrowedBookRepository;
    private final MailBuilder mailBuilder;
    private final MailSender mailSender;
    private final UserService userService;
    private final BookService bookService;

    @Async
    public void checkBorrowedBooksReturnDates() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        final Date currentDate = calendar.getTime();
        List<BorrowedBookEntity> borrowedBookEntities = borrowedBookRepository.findAll();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookEntities) {
            if (borrowedBookEntity.isAccepted()) {
                final Date borrowedBookReturnDate = borrowedBookEntity.getReturnDate();
                long daysToReturnDate = TimeUnit.MILLISECONDS
                        .toDays(borrowedBookReturnDate.getTime() - currentDate.getTime());

                final UserEntity userEntity = userService.getUserById(borrowedBookEntity.getUserId());
                final BookEntity bookEntity = bookService.getBookById(borrowedBookEntity.getBookId());

                if (isBorrowedBookOverdue(currentDate, borrowedBookReturnDate)) {
                    increasePenalty(borrowedBookEntity, bookEntity, userEntity);
                } else if (isBorrowedBookReturnDateComing(daysToReturnDate)) {
                    sendComingUpBorrowedBookReturnDateMail(userEntity, borrowedBookEntity, bookEntity, daysToReturnDate);
                }
            }
        }
    }

    private void increasePenalty(BorrowedBookEntity borrowedBookEntity, BookEntity bookEntity, UserEntity userEntity) {
        borrowedBookEntity.setPenalty(borrowedBookEntity.getPenalty().add(PENALTY));
        borrowedBookRepository.save(borrowedBookEntity);
        sendIncreasedPenaltyInformationMail(userEntity, borrowedBookEntity, bookEntity);
        log.info("New penalty added for borrowed book with id " + borrowedBookEntity.getId());
    }

    private boolean isBorrowedBookOverdue(Date currentDate, Date borrowedBookReturnDate) {
        return currentDate.compareTo(borrowedBookReturnDate) > 0;
    }

    private boolean isBorrowedBookReturnDateComing(long daysToReturnDate) {
        return daysToReturnDate <= INCOMING_RETURN_DATE_REMINDER_PERIOD;
    }

    private void sendIncreasedPenaltyInformationMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity,
                                                     BookEntity bookEntity) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getOrderedBookPenaltyInformationMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getAuthor(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliate(),
                        borrowedBookEntity.getPenalty()),
                "Penalty increased for an overdue book");
        log.info("New sendIncreasedPenaltyInformationMail message sent to " + userEntity.getUsername());
    }

    private void sendComingUpBorrowedBookReturnDateMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity,
                                                        BookEntity bookEntity, long daysToReturnDate) {
        mailSender.send(userEntity.getUsername(), mailBuilder.getComingUpBorrowedBookReturnDateMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getAuthor(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliate(),
                        daysToReturnDate),
                "Incoming book's return date");
        log.info("New sendComingUpBorrowedBookReturnDateMail message sent to " + userEntity.getUsername());
    }

}
