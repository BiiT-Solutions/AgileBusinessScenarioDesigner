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

	public static Integer returnYearDistanceFromDate(Object object) {
		if (object instanceof Date) {
			return returnYearDistanceFromDate((Date) object);
		}
		return null;
	}

	public static Integer returnYearDistanceFromDate(Date date) {
		Calendar now = Calendar.getInstance();
		Calendar compareDate = Calendar.getInstance();
		compareDate.setTime(date);

		int diff = now.get(Calendar.YEAR) - compareDate.get(Calendar.YEAR);
		// if ((now.get(Calendar.MONTH) > compareDate.get(Calendar.MONTH))
		// || ((now.get(Calendar.MONTH) == compareDate.get(Calendar.MONTH)) &&
		// (now.get(Calendar.DATE) > compareDate
		// .get(Calendar.DATE)))) {
		// diff--;
		// }
		return diff;
	}

	public static Integer returnMonthDistanceFromDate(Object object) {
		if (object instanceof Date) {
			return returnMonthDistanceFromDate((Date) object);
		}
		return null;
	}

	public static Integer returnMonthDistanceFromDate(Date date) {
		Calendar now = Calendar.getInstance();
		Calendar compareDate = Calendar.getInstance();
		compareDate.setTime(date);
		return ((returnYearDistanceFromDate(date) * 12) + compareDate.get(Calendar.MONTH)) - now.get(Calendar.MONTH);
	}

	public static Integer returnDaysDistanceFromDate(Object object) {
		if (object instanceof Date) {
			return returnDaysDistanceFromDate((Date) object);
		}
		return null;
	}

	public static Integer returnDaysDistanceFromDate(Date date) {
		Calendar now = Calendar.getInstance();
		Calendar compareDate = Calendar.getInstance();
		compareDate.setTime(date);
		return (int) ((now.getTimeInMillis() - compareDate.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	public static Date returnCurrentDate() {
		return Calendar.getInstance().getTime();
	}
}
