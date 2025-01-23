package com.ems.servicefinder.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javax.xml.stream.events.StartDocument;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Local;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.web.multipart.MultipartFile;

import com.ems.domain.Employee;
import com.ems.domain.LeaveAccount;
import com.ems.domain.LeaveHistoryDetails;
import com.ems.exception.ServiceException;

/**
 * Utility class to the user.
 * 
 * @author Avinash Tyagi
 * @version 2.0
 */
public class UserUtils {

	final static String LETTERS = "ABCDEFGHIJLMNOPQRSTUVXZ123456789";

	final static int LENGTH = 40;

	private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

	/**
	 * Generates a random password.
	 *
	 * @return the random password
	 */
	public static String randomPassword() {
		int i = 0;

		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		while (i < LENGTH) {
			char randomChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
			sb.append(randomChar);
			i++;
		}

		return sb.toString();
	}

	/**
	 * Check file size based on its type
	 * 
	 * @param size
	 *            limit
	 * @param file
	 *            MultipartFile file size
	 * @param message
	 *            file name
	 * @param isPDF
	 *            is a pdf file
	 * @param isImage
	 *            is an image file
	 * @param isOtherType
	 *            is not pdf or image
	 * @throws ServiceException
	 */
	public static void validateFileSize(long size, MultipartFile file, String message, boolean isPDF, boolean isImage,
			boolean isOtherType) throws ServiceException {
		if (isPDF && file.getSize() / 1024 > size) {
			throw new ServiceException(
					"The " + message + " file is too big, you should upload a file with a maximum of " + size + " KBs");
		} else if (isImage && file.getSize() / 1024 > size) {
			throw new ServiceException(
					"The " + message + " file is too big, you should upload a file with a maximum " + size + " KBs");
		} else if (isOtherType && file.getSize() / 1024 > size) {
			throw new ServiceException(
					"The " + message + " file is too big, you should upload a file with a maximum " + size + " KBs");
		}
	}

	/**
	 * Get standard asset name for firm
	 * 
	 * @param originalFileName
	 *            the original file name
	 * @param standardFileName
	 *            the standard file name
	 * @return the standard asset name
	 */
	public static String getAssetStandardName(String originalFileName, String standardFileName) {
		if (StringUtils.isNotEmpty(FilenameUtils.getExtension(originalFileName)))
			return standardFileName + "." + FilenameUtils.getExtension(originalFileName);
		else
			return standardFileName;
	}

	/**
	 * 
	 * Util method that cleans any special characters other than (), -,_ It
	 * replaces the Invalid characters with an underscore "_"
	 *
	 * @param fileName
	 * @return fileName without special characters other than space, (), -,_
	 */
	public static String validateSpecialCharacters(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return fileName; // just return it how it is.
		} else if (StringUtils.isNotEmpty(FilenameUtils.getExtension(fileName))) {
			String name = fileName.substring(0, fileName.lastIndexOf('.'));
			String extension = FilenameUtils.getExtension(fileName);
			name = name.replaceAll("[\\\\/:*?\"<>#| .%,=+;!@&\\[\\]]", "-");
			return name.concat(".").concat(extension);
		} else {
			return fileName.replaceAll("[\\\\/:*?\"<>#| .%,=+;!@&\\[\\]]", "-");
		}
	}

	/**
	 * Get the context path
	 * 
	 * @param firmName
	 *            the firm name
	 * @param user
	 *            the user
	 * @param assetsType
	 *            the asset type
	 * @param folderName
	 *            the folder name
	 * @return the context path
	 */
	public static String getPath(Employee employee) {

		return "user" + File.separator + employee.getId() + File.separator;

	}

	public static boolean isEmployeeJoiningGreaterThan(Employee employee,LocalDate checkingDate, int months) {
		return (Months.monthsBetween(new LocalDate(employee.getJoiningDate()), checkingDate).getMonths() >= months);
	}
	/**
	 * To convert DateTime into given format.
	 * 
	 * @param dateTime
	 *            the DateTime dateTime.
	 * 
	 * @param format
	 *            the String format.
	 * 
	 * @return String into given format.
	 */
	public static String dateTimeFormat(DateTime dateTime, String format) {
		return dateTime.toString(DateTimeFormat.forPattern(format));
	}
	 public static String toCamelCase(String inputString) {
	       String result = "";
	       if (inputString.length() == 0) {
	           return result;
	       }
	       char firstChar = inputString.charAt(0);
	       char firstCharToUpperCase = Character.toUpperCase(firstChar);
	       result = result + firstCharToUpperCase;
	       for (int i = 1; i < inputString.length(); i++) {
	           char currentChar = inputString.charAt(i);
	           char previousChar = inputString.charAt(i - 1);
	           if (previousChar == ' ') {
	               char currentCharToUpperCase = Character.toUpperCase(currentChar);
	               result = result + currentCharToUpperCase;
	           } else {
	               char currentCharToLowerCase = Character.toLowerCase(currentChar);
	               result = result + currentCharToLowerCase;
	           }
	       }
	       return result;
	   }
}
