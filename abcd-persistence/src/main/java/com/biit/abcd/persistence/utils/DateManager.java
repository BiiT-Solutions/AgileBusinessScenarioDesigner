package com.biit.abcd.persistence.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {
	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String convertDateToString(Date date) {
		if (date == null) {
			return new SimpleDateFormat(DATE_FORMAT).format(new Timestamp(new java.util.Date(0).getTime()));
		}
		return new SimpleDateFormat(DATE_FORMAT).format(date);
	}

	public static String convertDateToString(Timestamp time) {
		Date date = new Date(time.getTime());
		return convertDateToString(date);
	}

}
