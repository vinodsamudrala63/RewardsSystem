package com.java.banking.util;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RewardsDateUtil {

	public static String getMonthYearFromTimeStamp(Timestamp timestamp) {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime((Date) timestamp.clone());
		return mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-"
				+ mCalendar.get(Calendar.YEAR);
	}

	public static String getMonthNameAndYear(int previousMonth) {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.add(Calendar.MONTH, -previousMonth);
		return mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-"
				+ mCalendar.get(Calendar.YEAR);
	}

	public static Timestamp getFirstDateOfMonth(int previousMonth) {
		LocalDate localDate = LocalDate.now();
		LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
		return Timestamp.valueOf(startOfDay.minusMonths(previousMonth).with(firstDayOfMonth()));
	}

	public static Timestamp getLastDateOfMonths(int previousMonth) {
		LocalDate localDate = LocalDate.now();
		LocalDateTime endOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
		return Timestamp.valueOf(endOfDay.minusMonths(previousMonth).with(lastDayOfMonth()));
	}

}
