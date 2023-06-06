package com.system.libsystem.scheduler.penalty;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookService;
import com.system.libsystem.entities.borrowedbook.BorrowedBookEntity;
import com.system.libsystem.entities.borrowedbook.BorrowedBookRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.mail.MailBuilder;
import com.system.libsystem.mail.MailSender;
import com.system.libsystem.scheduler.SchedulerDateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class ReturnDateCheckerTask {

    private static final BigDecimal PENALTY = new BigDecimal("1.00");
    private static final int INCOMING_RETURN_DATE_REMINDER_PERIOD = 7;

    private final BorrowedBookRepository borrowedBookRepository;
    private final MailSender mailSender;
    private final UserService userService;
    private final BookService bookService;

    public void checkBorrowedBooksReturnDates() {
        final Date currentDate = SchedulerDateUtil.getCurrentDate();
        final List<BorrowedBookEntity> borrowedBookEntities = borrowedBookRepository.findAll();

        for (BorrowedBookEntity borrowedBookEntity : borrowedBookEntities) {
            if (borrowedBookEntity.isAccepted() && !borrowedBookEntity.isClosed()) {
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
        mailSender.send(userEntity.getUsername(), MailBuilder.getOrderedBookPenaltyInformationMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getFormattedAuthorsAsString(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliateEntity().getName(),
                        borrowedBookEntity.getPenalty()),
                "Penalty increased for an overdue book");
        log.info("New sendIncreasedPenaltyInformationMail message sent to " + userEntity.getUsername());
    }

    private void sendComingUpBorrowedBookReturnDateMail(UserEntity userEntity, BorrowedBookEntity borrowedBookEntity,
                                                        BookEntity bookEntity, long daysToReturnDate) {
        mailSender.send(userEntity.getUsername(), MailBuilder.getComingUpBorrowedBookReturnDateMailBody(
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bookEntity.getTitle(),
                        bookEntity.getFormattedAuthorsAsString(),
                        borrowedBookEntity.getReturnDate().toString(),
                        borrowedBookEntity.getAffiliateEntity().getName(),
                        daysToReturnDate),
                "Incoming book's return date");
        log.info("New sendComingUpBorrowedBookReturnDateMail message sent to " + userEntity.getUsername());
    }

}
