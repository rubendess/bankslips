package com.bankslips.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtils {

    public static long getDaysDiff(Date startDate, Date endDate) {
        if(startDate == null || endDate == null)
            throw new RuntimeException("Invalid params");

        LocalDate date1 = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Duration.between(date1.atStartOfDay(), date2.atStartOfDay()).toDays();
    }
}
