package com.ems.framework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import com.ems.servicefinder.utils.Constants;

@Component
public class DateUtil {

	/**
	 * Reset the time from date (set to 00:00:00)
	 *
	 * @param date
	 *            the Date
	 * @return the Date
	 */
	public static Date resetTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Reset the time from date (set to 00:00:00)
	 *
	 * @param date
	 *            the Date
	 * @return the Date
	 */
	public static DateTime resetTime(DateTime date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date.toDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new DateTime(cal.getTime());
	}

	/**
	 * Reset the time from date (set to 00:00:00)
	 *
	 * @param date
	 *            the Date
	 * @return the Date
	 */
	public static DateTime maxTime(DateTime date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date.toDate());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return new DateTime(cal.getTime());
	}

	/**
	 * Reset the time from date (set to 00:00:00)
	 *
	 * @param date
	 *            the Date
	 * @return the Date
	 */
	public static Date maxTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Remove the time from date
	 *
	 * @param date
	 *            the Date
	 * @param format
	 *            the date format (like "MM/dd/YYYY")
	 * @return the date string in above format
	 */
	public static String removeTime(Date date, String format) {
		DateFormat outputFormatter = new SimpleDateFormat(format);
		return outputFormatter.format(date);
	}

	/**
	 * Remove the time from date
	 *
	 * @param date
	 *            the Date
	 * @param format
	 *            the date format (like "MM/dd/YYYY")
	 * @return the date string in above format
	 */
	public static String removeTime(DateTime date, String format) {
		DateFormat outputFormatter = new SimpleDateFormat(format);
		return outputFormatter.format(date.toDate());
	}

	/**
	 * Remove the time from date in and produce date in "dd/MM/yyyy" format
	 * 
	 * @param date
	 *            the Date
	 * @return the date string
	 */
	public static String removeTime(Date date) {
		DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
		return outputFormatter.format(date);
	}

	/**
	 * Remove the time from date in and produce date in "dd/MM/yyyy" format
	 * 
	 * @param date
	 *            the Date
	 * @return the date string
	 */
	public static String removeTime(DateTime date) {
		DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
		return outputFormatter.format(date.toDate());
	}

	/**
	 * Check if both date belongs to same financial year.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return boolean
	 */
	public static boolean checkIfBothInSameFinancialYear(LocalDate startDate, LocalDate endDate) {
		return getFinancialYear(startDate) == getFinancialYear(endDate);

	}

	/**
	 * Financial year of the date given as an int.
	 * 
	 * @param startDate
	 * @return financial year of the date given as an int.
	 * @author Sarit Mukherjee
	 */
	public static int getFinancialYear(LocalDate startDate) {
		LocalDate start = null;
		if (Constants.FINANCIAL_YEAR_START_MONTH <= startDate.getMonthOfYear() && startDate.getMonthOfYear() <= 12) {
			start = new LocalDate(startDate.getYear(), Constants.FINANCIAL_YEAR_START_MONTH, 1);
		} else {
			start = new LocalDate(startDate.getYear() - 1, Constants.FINANCIAL_YEAR_START_MONTH, 1);
		}
		return start.getYear();
	}

	/**
	 * Return First or Last Date of the financial year given as per flag. If
	 * flag is true then start date otherwise end day.
	 * 
	 * @param startDate
	 * @param startDateOrEndDate
	 * @return start or end date as mentioned in the flag
	 * @author Sarit Mukherjee
	 */
	public static LocalDate getFirstOrLastDateOfFinancialYear(LocalDate startDate, boolean startDateOrEndDate) {
		LocalDate start = getFirstOrLastDateOfFinancialYear(getFinancialYear(startDate), true);
		LocalDate end = getFirstOrLastDateOfFinancialYear(getFinancialYear(startDate), false);
		if (startDateOrEndDate)
			return start;
		else
			return end;
	}

	/**
	 * Return First or Last Date of the financial year given as per flag. If
	 * flag is true then start date otherwise end day.
	 * 
	 * @param startDate int format.
	 * @param startDateOrEndDate
	 * @return start or end date as mentioned in the flag
	 * @author Sarit Mukherjee
	 */
	public static LocalDate getFirstOrLastDateOfFinancialYear(int startDate, boolean startDateOrEndDate) {
		LocalDate start = new LocalDate(startDate, Constants.FINANCIAL_YEAR_START_MONTH, 1);
		LocalDate end = new LocalDate(startDate + 1, Constants.FINANCIAL_YEAR_START_MONTH - 1, 1).dayOfMonth()
				.withMaximumValue();
		if (startDateOrEndDate)
			return start;
		else
			return end;
	}

}