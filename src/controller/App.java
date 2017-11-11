package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import utils.Config;
import utils.StringWithoutEqual;
import view.InitFrame;
import view.MainFrame;

/**
 * 
 * @author Pawel Paszki
 * 
 *         This is the controller of the application. It uses InitFrame to get
 *         user credentials and upon successful login creates MainFrame used for
 *         CRUD operations on Employee table
 *
 */
public class App implements ActionListener {

	/** init frame allows to enter db user credentials */
	private InitFrame initFrame;
	/** used to connect to DB when required */
	private Connection connection;
	/** used to execute statements on the DB */
	private Statement statement;
	/** resultSet obtained after a query is executed */
	private ResultSet resultSet;
	/** frame representing main UI */
	private MainFrame mainFrame;
	/** used to control DB position of current Employee */
	private int currentTableItem = 0;
	/** Employee can be updated - text fields are enabled */
	private boolean updateModeOn;
	/** Employee can be added - all text fields are enabled and cleared */
	private boolean addModeOn;
	/** stores all SSNs of Employees */
	private ArrayList<Integer> employeeIds = new ArrayList<>();

	private ArrayList<Integer> visibleButtonsIndices = new ArrayList<>();

	/** hardcoded number of columns in the table */
	private final int columnNumber = 9;

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/**
	 * The name of the database we are testing with (this default is installed with
	 * MySQL)
	 */
	private final String dbName = "Assignment1";

	/** The name of the table we are testing with */
	private final String tableName = "Employee";

	public static void main(String[] args) {
		App app = new App();
		app.init(app);
	}

	/**
	 * Initial frame is initialized here and app instance is passed
	 * 
	 * @param app
	 *            is used to pass all performed actions from view to the App class
	 */
	private void init(App app) {
		initFrame = new InitFrame(app);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		switch (action) {
		case "clear":
			mainFrame.clearAllTextFields();
			break;
		case "cancel":
			/** When cancel is pressed - current Employees data is restored*/
			if(currentTableItem > 0) {
				try {
					executeGetQuery(employeeIds.get(currentTableItem - 1), "comboSelected");
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			hideWhenEditable(false);
			for (int i = 0; i < mainFrame.getButtons().size(); i++) {
				if (visibleButtonsIndices.contains(i)) {
					mainFrame.getButtons().get(i).setVisible(true);
				}
			}
			addModeOn = false;
			updateModeOn = false;
			mainFrame.enableTextFields(false);
			mainFrame.getExceptionStackDisplayArea().setVisible(false);
			break;
		case "name changed":
			/** action to be performed, when an Employee from dropdown is selected*/
			int selectedEmployee = mainFrame.getEmpNameComboBox().getSelectedIndex();
			currentTableItem = selectedEmployee + 1;
			int selectedEmpSsn = employeeIds.get(selectedEmployee);
			try {
				executeGetQuery(selectedEmpSsn, "comboSelected");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;
		case "authenticate":
			/** user name and password are gathered from textfields */
			/** and attempt to create connection is made */
			String user = initFrame.getUserNameTextField().getText();
			String password = initFrame.getPasswordTextField().getText();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(
						"jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, user, password);
				initFrame.getFrame().dispose();
				mainFrame = new MainFrame(this);
				getDBData();
			} catch (Exception e) {
				/**
				 * if any of values specified in getConnection are invalid exception stacktrace
				 * is printed to the console assumption is made that user and/or password are
				 * incorrect
				 */

				initFrame.getPromptLabel().setText("Please enter valid credentials");
				initFrame.getPromptLabel().setForeground(new Color(255, 0, 0));
			}
			break;
		case "prev":
			populatePreviousEmployeeData();
			break;
		case "next":
			populateNextEmployeeData();
			break;
		case "add":
			if (addModeOn) {
				addEmployeeItem();
			} else {
				addModeOn = true;
				updateVisibleButtonsIndices();
				hideWhenEditable(addModeOn);
				mainFrame.enableTextFields(addModeOn);
				mainFrame.hideOtherButtons(false, 2);
				mainFrame.clearAllTextFields();
			}
			break;
		case "update":
			if (updateModeOn) {
				updateEmployeeItem();
			} else {
				if(employeeIds.size() > 0) {
					updateModeOn = true;
					updateVisibleButtonsIndices();
					hideWhenEditable(updateModeOn);
					mainFrame.hideOtherButtons(false, 3);
					mainFrame.enableTextFields(updateModeOn);
				}
			}
			break;
		case "remove":
			removeCurrentEmployeeItem();
			break;
		}
	}

	/**
	 * used to keep the visibility state for all buttons before entering 
	 * into add or update mode
	 */
	private void updateVisibleButtonsIndices() {
		visibleButtonsIndices.clear();
		for (int i = 0; i < mainFrame.getButtons().size(); i++) {
			if (mainFrame.getButtons().get(i).isVisible()) {
				visibleButtonsIndices.add(i);
			}
		}
	}

	/**
	 * 
	 * @param hide - hide/show components during or after add/update 
	 */
	private void hideWhenEditable(boolean hide) {
		mainFrame.getEmpNameComboBox().setVisible(!hide);
		mainFrame.getTextFields().get(2).setVisible(hide);
		mainFrame.getCancelButton().setVisible(hide);
		mainFrame.getClearButton().setVisible(hide);
	}

	/**
	 * This method removes currently displayed Employee from the table
	 */
	private void removeCurrentEmployeeItem() {
		if (employeeIds.size() > 0) {
			int id = employeeIds.get(--currentTableItem);
			try {
				statement = connection.createStatement();
				statement.executeUpdate("delete from " + tableName + " where Ssn=" + id);
				employeeIds.remove(Integer.valueOf(id));
				/**
				 * if there are no more Employees in the table after removal, all TextFields are
				 * cleared and all buttons but "add" button are disabled. otherwise Employee
				 * immediately next to removed Employee is displayed
				 */
				if (employeeIds.size() == 0) {
					mainFrame.clearAllTextFields();
					mainFrame.hideOtherButtons(true, 2);
					
				} else {
					if (currentTableItem == 0) {
						currentTableItem++;
						enablePreviousButton(false);
					}
					if (employeeIds.size() == 1) {
						enableNextButton(false);
						enablePreviousButton(false);
					}
					id = employeeIds.get(currentTableItem - 1);
					executeGetQuery(id, "remove");
				}
				updateCurrentEmployeeLabel();
				updateComboBoxValues();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method assigns SSN of previous Employee from the ArrayList and gets this
	 * Employee from the DB
	 */
	private void populatePreviousEmployeeData() {
		if (currentTableItem > 1) {
			int id = employeeIds.get(currentTableItem - 2);
			try {
				executeGetQuery(id, "previous");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method assigns SSN of next Employee from the ArrayList and gets this
	 * Employee from the DB
	 */
	private void populateNextEmployeeData() {
		if (currentTableItem < employeeIds.size()) {
			int id = employeeIds.get(currentTableItem);
			try {
				executeGetQuery(id, "next");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method executes "executeQuery" in order to obtain data about Employee
	 * from the DB
	 * 
	 * @param id
	 *            - SSN of the Employee
	 * @param buttonAction
	 *            - which button was pressed
	 * @throws SQLException
	 *             - if an SQLException occurs
	 */
	private void executeGetQuery(int id, String buttonAction) throws SQLException {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select * from " + tableName + " where Ssn=" + id);
		while (resultSet.next()) {
			/**
			 * populate text fields
			 */
			for (int i = 0; i < columnNumber; i++) {
				mainFrame.getTextFields().get(i).setText(resultSet.getString(Config.columnNames[i]));
			}

			/**
			 * depending on button pressed, certain buttons' states and current Employee's
			 * indicator need to be updated
			 */
			switch (buttonAction) {
			case "next":
				enablePreviousButton(true);
				currentTableItem++;
				if (currentTableItem == employeeIds.size()) {
					enableNextButton(false);
				}
				break;
			case "previous":
				currentTableItem--;
				if (currentTableItem == 1) {
					enablePreviousButton(false);
				}
				enableNextButton(true);
				break;
			case "remove":
				// no further actions
				break;
			case "comboSelected":
				if (currentTableItem == 1) {
					enablePreviousButton(false);
				} else {
					enablePreviousButton(true);
				}
				if (currentTableItem == employeeIds.size()) {
					enableNextButton(false);
				} else if (currentTableItem < employeeIds.size()) {
					enableNextButton(true);
				}
				break;
			}
			updateCurrentEmployeeLabel();
			mainFrame.getEmpNameComboBox().setSelectedIndex(currentTableItem - 1);
		}
		// statement.close();
	}

	/**
	 * This method adds new Employee to the table, if all fiels are valid, otherwise
	 * the error message will be displ)ayed and user is expected to change input to
	 * conform to the table structure
	 */
	private void addEmployeeItem() {
		try {
			String columnNamesPartialQuery = "(";
			for (int i = 1; i < columnNumber - 1; i++) {
				columnNamesPartialQuery += Config.columnNames[i] + ", ";
			}
			columnNamesPartialQuery += Config.columnNames[8] + ") ";
			String columnValuesPartialQuery = "";
			String delimiter = "";
			for (int i = 1; i < columnNumber - 1; i++) {
				if (Config.stringValuesIndices.contains(i)) {
					delimiter = "\'";
				} else {
					delimiter = "";
				}
				columnValuesPartialQuery += delimiter + mainFrame.getTextFields().get(i).getText() + delimiter + ", ";
			}
			columnValuesPartialQuery += mainFrame.getTextFields().get(8).getText() + ")";
			String query = "insert into Employee " + columnNamesPartialQuery + "values (" + columnValuesPartialQuery;
			executeUpdateQuery(query);
			/**
			 * after adding new Employee to the table, this employee is retrieved from the
			 * DB, displayed and its SSN is added to the list of all SSNs
			 */
			getLastEmployee();
			if (currentTableItem > 1) {
				enablePreviousButton(true);
			}
			updateComboBoxValues();
			updateCurrentEmployeeLabel();
			hideWhenEditable(addModeOn);
		} catch (SQLException e) {
			displaySyntaxError();
		}
	}

	/**
	 * This method updates currently displayed Employee if all fields are valid,
	 * otherwise the error message will be displayed and user is expected to change
	 * input to conform to the table structure
	 */
	private void updateEmployeeItem() {
		try {
			String partialQuery = "";
			String delimiter = "";
			for (int i = 1; i < columnNumber - 1; i++) {
				if (Config.stringValuesIndices.contains(i)) {
					delimiter = "\'";
				} else {
					delimiter = "";
				}
				partialQuery += Config.columnNames[i] + " = " + delimiter + mainFrame.getTextFields().get(i).getText()
						+ delimiter + ",";
			}
			partialQuery += Config.columnNames[8] + " = " + mainFrame.getTextFields().get(8).getText();

			String query = "update Employee set " + partialQuery + " where ssn = "
					+ employeeIds.get(currentTableItem - 1);
			executeUpdateQuery(query);
			if (currentTableItem < employeeIds.size()) {
				enableNextButton(true);
			}
			updateModeOn = false;
			updateComboBoxValues();
			hideWhenEditable(updateModeOn);
		} catch (SQLException e) {
			displaySyntaxError();
		}
	}

	/**
	 * 
	 */
	private void updateComboBoxValues() {
		try {
			DefaultComboBoxModel<StringWithoutEqual> model = new DefaultComboBoxModel<>();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from " + tableName);
			while (resultSet.next()) {
				model.addElement(new StringWithoutEqual(resultSet.getString("Name")));
			}
			mainFrame.getEmpNameComboBox().setModel(model);
			if(model.getSize() > 0) {
				mainFrame.getEmpNameComboBox().setSelectedIndex(currentTableItem - 1);
			} else {
				mainFrame.getPreviousButton().setVisible(false);
				mainFrame.getNextButton().setVisible(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method executes update query based on the String parameter
	 * 
	 * @param query
	 *            - query to execute
	 * @throws SQLException
	 *             - when an error occurs
	 */
	private void executeUpdateQuery(String query) throws SQLException {
		statement = connection.createStatement();
		statement.executeUpdate(query);
		if (currentTableItem > 1) {
			enablePreviousButton(true);
		}
		enableAddButton(true);
		enableRemoveButton(true);
		enableUpdateButton(true);
		mainFrame.enableTextFields(false);
		mainFrame.getExceptionStackDisplayArea().setVisible(false);
		statement.close();
	}

	/**
	 * This method makes error visible, if there is an invalid field during adding
	 * or updating an Employee
	 */
	private void displaySyntaxError() {
		mainFrame.getExceptionStackDisplayArea().setText("Something went wrong. Check if input is correct");
		mainFrame.getExceptionStackDisplayArea().setVisible(true);
		mainFrame.highlightTextFieldsInRed();
	}

	/**
	 * This is the method initially called, after the main frame is shown it
	 * traverses through all of the table entries and gathers all the SSNs of
	 * Employees, if there are any
	 */
	private void getDBData() {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from " + tableName);
			int counter = 0;
			DefaultComboBoxModel<StringWithoutEqual> model = mainFrame.getNameModel();
			model.removeAllElements();
			while (resultSet.next()) {
				model.addElement(new StringWithoutEqual(resultSet.getString("Name")));
				employeeIds.add(resultSet.getInt("Ssn"));
				if (counter++ == 0) {
					currentTableItem = 1;
					for (int i = 0; i < columnNumber; i++) {
						mainFrame.getTextFields().get(i).setText(resultSet.getString(Config.columnNames[i]));
					}
					enablePreviousButton(false);
				}
			}
			if (employeeIds.size() == 0) {
				mainFrame.hideOtherButtons(false, 2);
			} else {
				mainFrame.getEmpNameComboBox().setModel(model);
				mainFrame.getEmpNameComboBox().setSelectedIndex(0);
			}
			if (employeeIds.size() < 2) {
				enableNextButton(false);
			}
			updateCurrentEmployeeLabel();
			statement.close();
			mainFrame.getEmpNameComboBox().addActionListener(this);
			mainFrame.getEmpNameComboBox().setActionCommand("name changed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method retrieves last employee from the DB right after it is added to
	 * the table
	 */
	private void getLastEmployee() {
		try {
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("select * from " + tableName + " where ssn=(select max(ssn) from " + tableName + ")");
			int counter = 0;
			while (resultSet.next()) {
				employeeIds.add(resultSet.getInt("Ssn"));
				if (counter++ == 0) {
					currentTableItem = employeeIds.size();
					for (int i = 0; i < columnNumber; i++) {
						mainFrame.getTextFields().get(i).setText(resultSet.getString(Config.columnNames[i]));
					}
				}
			}
			addModeOn = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param value
	 *            - true enables, false disables update button
	 */
	private void enableUpdateButton(boolean value) {
		mainFrame.getUpdateButton().setVisible(value);
	}

	/**
	 * 
	 * @param value
	 *            - true enables, false disables previous button
	 */
	private void enablePreviousButton(boolean value) {
		mainFrame.getPreviousButton().setVisible(value);
	}

	/**
	 * 
	 * @param value
	 *            - true enables, false disables update button
	 */
	private void enableNextButton(boolean value) {
		mainFrame.getNextButton().setVisible(value);
	}

	/**
	 * 
	 * @param value
	 *            - true enables, false disables update button
	 */
	private void enableRemoveButton(boolean value) {
		mainFrame.getRemoveButton().setVisible(value);
	}

	/**
	 * 
	 * @param value
	 *            - true enables, false disables update button
	 */
	private void enableAddButton(boolean value) {
		mainFrame.getAddButton().setVisible(value);
	}

	/**
	 * This method updates label specifying current employee in the table and number
	 * of all employees in this table
	 */
	private void updateCurrentEmployeeLabel() {
		mainFrame.getCurrentEmployeeLabel().setText(currentTableItem + "/" + employeeIds.size());
	}
}
