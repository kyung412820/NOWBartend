package com.nowbartend.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtil {

    public static LocalDateTime getStartOfToday() {
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDateTime getEndOfToday() {
        return LocalDate.now().atStartOfDay().plusDays(1).minusNanos(1);
    }
}
