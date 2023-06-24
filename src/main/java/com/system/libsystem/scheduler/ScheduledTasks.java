package com.system.libsystem.scheduler;

import com.system.libsystem.scheduler.newbooks.NewBooksAddedCheckerTask;
import com.system.libsystem.scheduler.orderpickup.OrderPickUpCheckerTask;
import com.system.libsystem.scheduler.penalty.ReturnDateCheckerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final String RETURN_DATES_CHECK_PERIOD = "0 0 0 * * ?";
    private static final String ORDER_PICK_UP_CHECK_PERIOD = "0 0 0 * * ?";
    private static final String NEW_BOOKS_ADDED_CHECK_PERIOD = "0 0 0 * * ?";

    private final ReturnDateCheckerTask returnDateCheckerTask;
    private final OrderPickUpCheckerTask orderPickUpCheckerTask;
    private final NewBooksAddedCheckerTask newBooksAddedCheckerTask;

    @Scheduled(cron = RETURN_DATES_CHECK_PERIOD)
    public void checkBorrowedBooksReturnDatesTask() {
        returnDateCheckerTask.checkBorrowedBooksReturnDates();
        log.info("Scheduled task checkBorrowedBooksReturnDatesTask is running");
    }

    @Scheduled(cron = ORDER_PICK_UP_CHECK_PERIOD)
    public void checkIfOrdersWerePickedUpTask() {
        orderPickUpCheckerTask.checkIfOrderedBooksWerePickedUp();
        log.info("Scheduled task checkIfOrdersWerePickedUp is running");
    }

    @Scheduled(cron = NEW_BOOKS_ADDED_CHECK_PERIOD)
    public void updateNewAddedBooksTask() {
        newBooksAddedCheckerTask.updateNewAddedBooks();
        log.info("Scheduled task updateNewAddedBooks is running");
    }

}
