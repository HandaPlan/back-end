package com.org.candoit.global.util;

import java.time.LocalDate;

public class DateTimeUtil {

    public static int getWeekOfMonth(LocalDate date){
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int dayOfWeekOfFirst = firstDayOfMonth.getDayOfWeek().getValue();
        int dayOfMonth = date.getDayOfMonth();

        return (dayOfMonth + dayOfWeekOfFirst - 2) / 7 + 1;
    }
}
