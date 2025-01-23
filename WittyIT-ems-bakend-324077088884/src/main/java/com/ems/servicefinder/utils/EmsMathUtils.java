package com.ems.servicefinder.utils;

import java.math.BigDecimal;

public class EmsMathUtils {
	
	
	/**
	 * Rounds off a decimal value to decimal places
	 *
	 * @param number the number (like 4.12345)
	 * @param decimalPlaces upto how many places number has to be rounded off (like 2)
	 * @return roundedOffNumber (like 4.12)
	 */
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	/**
	 * Remove the trailing zeros.
	 *
	 */
	public static float removeTrailingZeros(float d) {
		try {
			String s = "" + d;
			s = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll(
					"\\.$", "");

			int index = s.lastIndexOf(".") > -1 ? s.lastIndexOf(".") : 0;

			return round(d, index);
		} catch (Exception e) {
			return d;
		}
	}
	

	/**
	 * Rounds off a decimal value to decimal places
	 *
	 * @param number the number (like 4.12345)
	 * @param decimalPlaces upto how many places number has to be rounded off (like 2)
	 * @return roundedOffNumber (like 4.12)
	 */
	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * Remove the trailing zeros.
	 *
	 */
	public static double removeTrailingZeros(double d) {
		try {
			String s = "" + d;
			s = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll(
					"\\.$", "");

			int index = s.lastIndexOf(".") > -1 ? s.lastIndexOf(".") : 0;

			return round(d, index);
		} catch (Exception e) {
			return d;
		}
	}

}
