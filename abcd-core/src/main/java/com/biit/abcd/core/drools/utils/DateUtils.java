package com.biit.abcd.core.drools.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Date returnCurrentDateMinusYears(int years) {
		Calendar now = Calendar.getInstance();
		// Substract the years from the calendar
		now.add(Calendar.YEAR, -years);
		return now.getTime();
	}

	public static Date returnCurrentDateMinusMonths(int months) {
		Calendar now = Calendar.getInstance();
		// Substract the months from the calendar
		now.add(Calendar.MONTH, -months);
		return now.getTime();
	}

	public static Date returnCurrentDateMinusDays(int days) {
		Calendar now = Calendar.getInstance();
		// Substract the days from the calendar
		now.add(Calendar.DATE, -days);
		return now.getTime();
	}

	public static Date returnCurrentDate() {
		return Calendar.getInstance().getTime();
	}
}
