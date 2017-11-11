package utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Pawel Paszki
 * This class stores certain constant values used in the application
 *
 */
public class Config {
	public static final String[] columnNames = { "Ssn", "Bdate", "Name", "Address", "Salary", "Sex", "Works_For",
			"Manages", "Supervises" };
	public static final ArrayList<Integer> stringValuesIndices = new ArrayList<>(Arrays.asList(1,2,3,5));
	public static final Color darkColor = new Color(33,33,33);
	public static final Color lightColor = new Color(238,238,238);
	public static final Color panelBackgroundColor = new Color(33,33,33);
	public static final Color labelsForegroundColor = new Color(189,189,189);
}
