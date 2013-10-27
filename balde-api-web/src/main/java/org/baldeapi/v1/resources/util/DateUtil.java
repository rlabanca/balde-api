package org.baldeapi.v1.resources.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static DateFormat getISODate() {
		return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	}
	
	public static long calcInDays(Date d1, Date d2) {
		long diff = Math.abs(d1.getTime() - d2.getTime());
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}
}
