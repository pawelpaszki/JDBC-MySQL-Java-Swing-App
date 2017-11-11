package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.App;
import utils.Config;
import utils.InputValidator;
import utils.StringWithoutEqual;

/**
 * 
 * @author Pawel Paszki
 *
 * This class is used to render the main GUI window
 */
public class MainFrame extends CustomFrame {

	// main frame of the rendered view
	private JFrame frame;
	
	// ArrayList of rendered text fields used to simplify 
	// handling those text fields
	private ArrayList<JTextField> textFields = new ArrayList<>();
	
	// ArrayList of rendered buttons used to simplify 
	// handling those buttons
	private ArrayList<JButton> buttons = new ArrayList<>();

	// button used to display previous Employee, if any available
	private JButton previousButton;
	// button used to display next Employee, if any available
	private JButton nextButton;
	// button used to allow updating currently displayed Employee
	private JButton updateButton;
	// button used to remove currently displayed Employee
	private JButton removeButton;
	// button used to add new Employee into the Table
	private JButton addButton;
	
	private JButton clearButton;
	
	private JButton cancelButton;
	// this label displays current Employee record and number of all employees
	// available in the database
	private JLabel currentEmployeeLabel;
	
	// This text area is displayed, when incorrect input is provided when updating
	// or adding new Employee
	private JTextArea exceptionStackDisplayArea;
	
	// used to select an Employee from the dropdown menu and display his/her record
	private JComboBox<StringWithoutEqual> empNameComboBox;
	
	// model of combobox
	private DefaultComboBoxModel<StringWithoutEqual> nameModel = new DefaultComboBoxModel<>();
	
	/**
	 * 
	 * @param parent - main App instance
	 * This is the constructor used to instantiate MainFrame class
	 */
	public MainFrame(App parent) {
		frame = new JFrame("Employee ");
		frame.setSize(500, 340);
		frame.setResizable(false); // resising disabled
		frame.setLocationRelativeTo(null); // center relative to desktop
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app, when 'x' pressed
		frame.setLayout(null); // no layout manager
		frame.setVisible(true);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 500, 340);
		mainPanel.setBackground(Config.panelBackgroundColor);
		mainPanel.setLayout(null);
		
		// create all labels used in the MainFrame and specify their coordinates and dimensions
		createLabel("Ssn: ", 5, 5, 100, 40);
		createLabel("Bdate: ", 270, 5, 100, 40);
		createLabel("Name: ", 5, 50, 100, 40);
		createLabel("Address: ", 5, 95, 100, 40);
		createLabel("Salary: ", 5, 140, 100, 40);
		createLabel("Sex: ", 270, 140, 100, 40);
		createLabel("Works for: ", 5, 185, 100, 40);
		createLabel("Manages: ", 270, 185, 100, 40);
		createLabel("Supervises: ", 5, 230, 100, 40);
		
		// create all text fields used in the MainFrame and specify their coordinates and dimensions
		textFields.add(createTextField(true, 110, 15, 100, 20));
		textFields.add(createTextField(true, 390, 15, 100, 20));
		textFields.add(createTextField(true, 110, 60, 380, 20));
		empNameComboBox = new JComboBox<>(nameModel);
		empNameComboBox.setBounds(110, 60, 380, 20);
		textFields.get(2).setVisible(false);
		textFields.add(createTextField(true, 110, 105, 380, 20));
		textFields.add(createTextField(true, 110, 150, 100, 20));
		textFields.add(createTextField(true, 390, 150, 100, 20));
		textFields.add(createTextField(true, 110, 195, 100, 20));
		textFields.add(createTextField(true, 390, 195, 100, 20));
		textFields.add(createTextField(true, 110, 240, 100, 20));
		
		// create all the buttons and current employee label
		buttons.add(previousButton = createButton("prev", parent, 5, 280, 60, 20));
		
		currentEmployeeLabel = new JLabel("/0");
		currentEmployeeLabel.setBounds(120, 280, 85, 20);
		currentEmployeeLabel.setFont(new Font("Arial", Font.BOLD, 14));
		currentEmployeeLabel.setForeground(Config.lightColor);
		mainPanel.add(currentEmployeeLabel);
		cancelButton = createButton("cancel", parent, 145, 280, 60, 20);
		cancelButton.setVisible(false);
		clearButton = createButton("clear", parent, 213, 280, 60, 20);
		clearButton.setVisible(false);
		buttons.add(nextButton = createButton("next", parent, 213, 280, 60, 20));
		buttons.add(addButton = createButton("add", parent, 281, 280, 60, 20));
		buttons.add(updateButton = createButton("update", parent, 350, 280, 60, 20));
		buttons.add(removeButton = createButton("remove", parent, 417, 280, 60, 20));
		
		// create text area used to indicate input errors
		exceptionStackDisplayArea = new JTextArea();
		exceptionStackDisplayArea.setBounds(5, 310, 480, 20);
		exceptionStackDisplayArea.setEditable(false);
		exceptionStackDisplayArea.setLineWrap(true);
		exceptionStackDisplayArea.setForeground(new Color(255,0,0));
		exceptionStackDisplayArea.setVisible(false);
		
		mainPanel.add(exceptionStackDisplayArea);
		mainPanel.add(empNameComboBox);
		
		frame.getContentPane().add(mainPanel);
	}
	
	/**
	 * This method is used to create JButton based on 
	 * specified parameters
	 * 
	 * @param text - text displayed inside button
	 * @param parent - class, which handles action listeners
	 * @param x - x coordinate inside mainPanel
	 * @param y - y coordinate inside mainPanel
	 * @param width - width of the button
	 * @param height - height of the button
	 * @return
	 */
	private JButton createButton(String text, App parent, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.addActionListener(parent);
		button.setBackground(Config.darkColor);
		button.setForeground(Config.lightColor);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBounds(x, y, width, height);
		mainPanel.add(button);
		return button;
	}


	/**
	 * @return the previousButton
	 */
	public JButton getPreviousButton() {
		return previousButton;
	}

	/**
	 * @param previousButton the previousButton to set
	 */
	public void setPreviousButton(JButton previousButton) {
		this.previousButton = previousButton;
	}

	/**
	 * @return the nextButton
	 */
	public JButton getNextButton() {
		return nextButton;
	}

	/**
	 * @param nextButton the nextButton to set
	 */
	public void setNextButton(JButton nextButton) {
		this.nextButton = nextButton;
	}

	/**
	 * @return the updateButton
	 */
	public JButton getUpdateButton() {
		return updateButton;
	}

	/**
	 * @param updateButton the updateButton to set
	 */
	public void setUpdateButton(JButton updateButton) {
		this.updateButton = updateButton;
	}

	/**
	 * @return the removeButton
	 */
	public JButton getRemoveButton() {
		return removeButton;
	}

	/**
	 * @param removeButton the removeButton to set
	 */
	public void setRemoveButton(JButton removeButton) {
		this.removeButton = removeButton;
	}

	/**
	 * @return the addButton
	 */
	public JButton getAddButton() {
		return addButton;
	}

	/**
	 * @param addButton the addButton to set
	 */
	public void setAddButton(JButton addButton) {
		this.addButton = addButton;
	}

	/**
	 * This method enables/ disables all of the text fields except Ssn text field
	 * @param value - determines, whether text fields will be enabled or disabled
	 */
	public void enableTextFields(boolean value) {
		currentEmployeeLabel.setVisible(!value);
		for(int i = 1; i < textFields.size(); i++) {
			textFields.get(i).setEditable(value);
			if(value) {
				textFields.get(i).setBackground(new Color(152,251,152));
			} else {
				textFields.get(i).setBackground(Color.WHITE);
			}
		}
	}

	/**
	 * This method highlights all editable text fields with incorrect input
	 * 
	 */
	public void highlightTextFieldsInRed() {
		for(int i = 1; i < textFields.size(); i++) {
			if(!InputValidator.isValid(textFields.get(i).getText(), i)) {
				textFields.get(i).setBackground(new Color(255,102,102));
			} else {
				textFields.get(i).setBackground(new Color(152,251,152));
			}
		}
	}
	
	/**
	 * This method is used to hide/show all buttons from buttons ArrayList but
	 * one specified by index 
	 * @param index - 
	 */
	public void hideOtherButtons(boolean show, int index) {
		for(int i = 0; i < buttons.size(); i++) {
			if (i == index) {
				continue;
			}
			buttons.get(i).setVisible(show);
		}
	}
	
	/**
	 * This method clears the text from all of the JTextFields
	 */
	public void clearAllTextFields() {
		for(int i = 0; i < textFields.size(); i++) {
			textFields.get(i).setText("");
		}
	}

	/**
	 * @return the exceptionStackDisplayArea
	 */
	public JTextArea getExceptionStackDisplayArea() {
		return exceptionStackDisplayArea;
	}

	/**
	 * @param exceptionStackDisplayArea the exceptionStackDisplayArea to set
	 */
	public void setExceptionStackDisplayArea(JTextArea exceptionStackDisplayArea) {
		this.exceptionStackDisplayArea = exceptionStackDisplayArea;
	}

	/**
	 * @return the currentEmployeeLabel
	 */
	public JLabel getCurrentEmployeeLabel() {
		return currentEmployeeLabel;
	}

	/**
	 * @param currentEmployeeLabel the currentEmployeeLabel to set
	 */
	public void setCurrentEmployeeLabel(JLabel currentEmployeeLabel) {
		this.currentEmployeeLabel = currentEmployeeLabel;
	}

	/**
	 * @return the textFields
	 */
	public ArrayList<JTextField> getTextFields() {
		return textFields;
	}

	/**
	 * @param textFields the textFields to set
	 */
	public void setTextFields(ArrayList<JTextField> textFields) {
		this.textFields = textFields;
	}

	/**
	 * @return the empIndexComboBox
	 */
	public JComboBox<StringWithoutEqual> getEmpNameComboBox() {
		return empNameComboBox;
	}

	/**
	 * @param empIndexComboBox the empIndexComboBox to set
	 */
	public void setEmpNameComboBox(JComboBox<StringWithoutEqual> empNameComboBox) {
		this.empNameComboBox = empNameComboBox;
	}

	/**
	 * @return the nameModel
	 */
	public DefaultComboBoxModel<StringWithoutEqual> getNameModel() {
		return nameModel;
	}

	/**
	 * @param nameModel the nameModel to set
	 */
	public void setNameModel(DefaultComboBoxModel<StringWithoutEqual> nameModel) {
		this.nameModel = nameModel;
	}

	/**
	 * @return the cancelButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * @param cancelButton the cancelButton to set
	 */
	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * @return the buttons
	 */
	public ArrayList<JButton> getButtons() {
		return buttons;
	}

	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(ArrayList<JButton> buttons) {
		this.buttons = buttons;
	}

	/**
	 * @return the clearButton
	 */
	public JButton getClearButton() {
		return clearButton;
	}

	/**
	 * @param clearButton the clearButton to set
	 */
	public void setClearButton(JButton clearButton) {
		this.clearButton = clearButton;
	}
}
