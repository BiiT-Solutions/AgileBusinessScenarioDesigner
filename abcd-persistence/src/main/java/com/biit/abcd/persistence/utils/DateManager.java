package com.biit.abcd.persistence.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateManager {
	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT_SIMPLE = "yyyy-MM-dd";

	public static String convertDateToString(Date date) {
		if (date == null) {
			return new SimpleDateFormat(DATE_FORMAT).format(new Timestamp(new java.util.Date(0).getTime()));
		}
		return new SimpleDateFormat(DATE_FORMAT).format(date);
	}

	public static String convertDateToString(Date date, String dateFormat) {
		if (date == null) {
			return new SimpleDateFormat(dateFormat).format(new Timestamp(new java.util.Date(0).getTime()));
		}
		return new SimpleDateFormat(dateFormat).format(date);
	}

	public static String convertDateToString(Timestamp time) {
		Date date = new Date(time.getTime());
		return convertDateToString(date);
	}

//	public static Date convertStringToDate(String date){
//		try {
//			return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault() ).parse(date);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static Date incrementDateOneDay(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);  // number of days to add
		return c.getTime();
	}

}
