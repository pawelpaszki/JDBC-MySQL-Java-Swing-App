package utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Pawel Paszki
 * This class is a util class used to validate text fields' input
 *
 */
public class InputValidator {

	/**
	 * 
	 * @param value - input to be validated
	 * @param textFieldIndex - index of textfield used to determine
	 * which validation is required
	 * @return true if valid, false otherwise
	 */
	public static boolean isValid(String value, int textFieldIndex) {
		switch(textFieldIndex) {
		case 1:
			return isValidDate(value);
		case 2:
			if(value.length() > 80 || value.length() == 0) {
				return false;
			} else {
				return true;
			}
		case 3:
			if(value.length() > 160 || value.length() == 0) {
				return false;
			} else {
				return true;
			}
		case 4:
			try {
				@SuppressWarnings("unused")
				double doubleValue = Double.parseDouble(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		case 5:
			ArrayList <String> sexValues = new ArrayList<>(Arrays.asList("m", "M", "f", "F"));
			if(sexValues.contains(value)) {
				return true;
			} else {
				return false;
			}
		case 6:
		case 7:
		case 8:
			try {
				@SuppressWarnings("unused")
				Integer intValue = Integer.parseInt(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param value - string representing date
	 * @return true, if valid date string, false otherwise
	 */
	private static boolean isValidDate(String value) {

			// Parse the date parts to integers
			String parts[] = value.split("-");
			int day, month, year;
			if(parts.length != 3) {
				return false;
			}
			try {
				year = Integer.parseInt(parts[0]);
				month = Integer.parseInt(parts[1]);
				day = Integer.parseInt(parts[2]);
			} catch (Exception e) {
				return false;
			}
			

			// Check the ranges of month and year
			if(year < 1000 || year > 3000 || month == 0 || month > 12)
				return false;

			int []  monthLength = new int [] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			// Adjust for leap years
			if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
				monthLength[1] = 29;

			// Check the range of the day
			return day > 0 && day <= monthLength[month - 1];

	}
}
