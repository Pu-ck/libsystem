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
public class SchedulerDateUtil {

    public static Date getCurrentDate() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long getDaysBetweenTwoDates(Date currentDate, Date bookDate) {
        long timeDifferenceBetweenDays = Math.abs(currentDate.getTime() - bookDate.getTime());
        return TimeUnit.DAYS.convert(timeDifferenceBetweenDays, TimeUnit.MILLISECONDS);
    }

}