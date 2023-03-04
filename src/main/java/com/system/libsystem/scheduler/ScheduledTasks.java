package com.system.libsystem.scheduler;

import com.system.libsystem.scheduler.penalty.ReturnDateChecker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ScheduledTasks {

    private static final String RETURN_DATES_CHECK_TIME_PERIOD = "0 0 0 * * ?";

    private final ReturnDateChecker returnDateChecker;

    @Scheduled(cron = RETURN_DATES_CHECK_TIME_PERIOD)
    public void checkBorrowedBooksReturnDatesTask() {
        returnDateChecker.checkBorrowedBooksReturnDates();
        log.info("Scheduled task checkBorrowedBooksReturnDatesTask is running");
    }

}
