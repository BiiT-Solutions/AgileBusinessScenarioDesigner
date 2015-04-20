package com.biit.abcd.core.drools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.biit.form.entity.TreeObject;

public class DateUtils {

	private static String DATE_FORMAT = "yyyy-mm-dd";

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

	public static Integer returnYearsDistanceFromDate(Object object) {
		if (object instanceof Date) {
			return returnYearsDistanceFromDate((Date) object);
		}
		return null;
	}

	public static Integer returnYearsDistanceFromDate(Date date) {
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

	public static Integer returnMonthsDistanceFromDate(Object object) {
		if (object instanceof Date) {
			return returnMonthsDistanceFromDate((Date) object);
		}
		return null;
	}

	public static Integer returnMonthsDistanceFromDate(Date date) {
		Calendar now = Calendar.getInstance();
		Calendar compareDate = Calendar.getInstance();
		compareDate.setTime(date);
		return ((returnYearsDistanceFromDate(date) * 12) + compareDate.get(Calendar.MONTH)) - now.get(Calendar.MONTH);
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

	public static Date transformLongStringToDate(String time) {
		Date date = new Date(Long.valueOf(time).longValue());
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DATE_FORMAT");
			return new SimpleDateFormat(DATE_FORMAT).parse(simpleDateFormat.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String questionDateYears(TreeObject question) {
		return "DateUtils.returnYearsDistanceFromDate(getAnswer('" + RulesUtils.getTreeObjectAnswerType(question)
				+ "'))";
	}
}
