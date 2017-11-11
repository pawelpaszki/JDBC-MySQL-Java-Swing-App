package view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Config;

/**
 * 
 * @author Pawel Paszki
 * This is the base frame, which is a parent of MainFrame and InitFrame
 * in order to prevent code duplication of common parts of two frames
 *
 */
public class CustomFrame {

	// main panel of the view
	protected JPanel mainPanel;
	
	/**
	 * This method is used to create all TextFields used to gather user input
	 * and add them to the main panel
	 * 
	 * @param disableEditing - set text field as disabled/ enabled
	 * @param x - x coordinate inside the panel
	 * @param y - y coordinate inside the panel
	 * @param width - width of the text field
	 * @param height - height of the text field
	 * @return instance of a JTextField  with specified attributes
	 */
	public JTextField createTextField(boolean disableEditing, int x, int y, int width, int height) {
		JTextField textField = new JTextField();
		textField.setBounds(x, y, width, height); 
		if(disableEditing) {
			textField.setEditable(false);
		}
		mainPanel.add(textField); // each of the TextFields is added to the main panel
		return textField;
	}
	
	/**
	 * This method is used to create label with specified parameters and add it to main panel
	 * 
	 * @param text -label's text
	 * @param x - x coordinate inside the panel
	 * @param y - y coordinate inside the panel
	 * @param width - width of the text field
	 * @param height - height of the text field
	 */
	protected void createLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, width, height);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		label.setForeground(Config.labelsForegroundColor);
		mainPanel.add(label);
	}
}
