package com.example.sabziwala.Service.communicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Helper class for handling ISO 8601 strings of the following format:
 * "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 */
public final class ISO8601 {
	/** Transform Calendar to ISO 8601 string. */
	public static String fromCalendar(final Calendar calendar) {
		Date date = calendar.getTime();
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	public static String fromDate(final Date date) {
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	/** Get current date and time formatted as ISO 8601 string. */
	public static String now() {
		return fromCalendar(GregorianCalendar.getInstance());
	}

	/** Transform ISO 8601 string to Calendar. */
	public static String formatDate(final String iso8601string, String timeZone, String format) throws ParseException {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date parseDate = sourceFormat.parse(iso8601string);
		SimpleDateFormat destFormat = new SimpleDateFormat(format);
		destFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		return destFormat.format(parseDate);
	}

	public static Date getDate(final String iso8601string, String timeZone) throws ParseException {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date parseDate = sourceFormat.parse(iso8601string);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		calendar.setTime(parseDate);
		return calendar.getTime();
	}

	public static Date getDateInUTC(final String iso8601string) throws ParseException {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sourceFormat.parse(iso8601string);
	}

	public static Date getDate(final String iso8601string) throws ParseException {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return sourceFormat.parse(iso8601string);
	}

	public static String getCurrentDateInUTC() {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return sourceFormat.format(calendar.getTime());
	}

	public static Date getDateWithFormat(String dateString, String formatString) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		Date date = format.parse(dateString);
		return date;
	}
}
