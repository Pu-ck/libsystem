package com.system.libsystem.scheduler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class SchedulerDateUtil {

    public static Date getCurrentDate() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long getDaysBetweenTwoDates(Date firstDate, Date secondDate) {
        long timeDifferenceBetweenDays = Math.abs(firstDate.getTime() - secondDate.getTime());
        return TimeUnit.DAYS.convert(timeDifferenceBetweenDays, TimeUnit.MILLISECONDS);
    }

}
