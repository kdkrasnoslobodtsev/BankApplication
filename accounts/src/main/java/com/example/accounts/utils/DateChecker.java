package com.example.accounts.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.*;

public class DateChecker {
    public static Date ValidateDate(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateStr);
            if (date.after(new Date())) {
                throw new IllegalArgumentException("Invalid date");
            }
            Calendar a = getCalendar(date);
            Calendar b = getCalendar(new Date());
            int diff = b.get(YEAR) - a.get(YEAR);
            if (a.get(MONTH) > b.get(MONTH) ||
                    (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
                diff--;
            }
            if (diff < 14 || 120 < diff) {
                throw new IllegalArgumentException("Inappropriate age");
            }
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Inappropriate date format");
        }
    }

    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(date);
        return calendar;
    }
}
