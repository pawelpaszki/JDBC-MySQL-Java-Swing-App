package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.App;
import utils.Config;

/**
 * 
 * @author Pawel Paszki
 * 
 * This class represents initial frame displayed, when the application is run
 * Its purpose is to gather user name and password and attempt to create connection
 * with SQL database
 *
 */
public class InitFrame extends CustomFrame {

	// view's container
	private JFrame frame;
	// Label displaying enter credentials prompt 
	private JLabel promptLabel;
	// text field used to get user's name input 
	private JTextField userNameTextField;
	// text field used to get user's password
	private JTextField passwordTextField;
	// authentication button
	private JButton authenticateButton;
	
	/**
	 * 
	 * @param parent - main App instance
	 * This is the constructor used to instantiate InitFrame class
	 */
	public InitFrame(App parent) {
		frame = new JFrame("Welcome");
		frame.setSize(300, 200); 
		frame.setResizable(false); // block resizing the frame
		frame.setLocationRelativeTo(null); // center relative to desktop
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app, when "x" buttomn is pressed
		frame.setLayout(null); // no layout manager used - all bounds are set manually
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 300, 200);
		mainPanel.setBackground(Config.panelBackgroundColor);
		mainPanel.setLayout(null); // no layout manager used
		
		promptLabel = new JLabel("Please enter your credentials");
		promptLabel.setBounds(5, 5, 280, 40);
		promptLabel.setFont(new Font("Arial", Font.BOLD, 14));
		promptLabel.setForeground(new Color(197,202,201));
		mainPanel.add(promptLabel);
		
		createLabel("User: ", 5, 50, 100, 40);
		
		createLabel("Password: ", 5, 95, 100, 40);
		
		setUserNameTextField(createTextField(false, 105, 60, 180, 20));
		
		setPasswordTextField(createTextField(false, 105, 105, 180, 20));
		
		authenticateButton = new JButton("authenticate");
		authenticateButton.setBounds(5, 145, 280, 30);
		authenticateButton.setBackground(Config.darkColor);
		authenticateButton.setForeground(Config.lightColor);
		authenticateButton.addActionListener(parent);
		mainPanel.add(authenticateButton);
		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the promptLabel
	 */
	public JLabel getPromptLabel() {
		return promptLabel;
	}

	/**
	 * @param promptLabel the promptLabel to set
	 */
	public void setPromptLabel(JLabel promptLabel) {
		this.promptLabel = promptLabel;
	}

	/**
	 * @return the userNameTextField
	 */
	public JTextField getUserNameTextField() {
		return userNameTextField;
	}

	/**
	 * @param userNameTextField the userNameTextField to set
	 */
	public void setUserNameTextField(JTextField userNameTextField) {
		this.userNameTextField = userNameTextField;
	}

	/**
	 * @return the passwordTextField
	 */
	public JTextField getPasswordTextField() {
		return passwordTextField;
	}

	/**
	 * @param passwordTextField the passwordTextField to set
	 */
	public void setPasswordTextField(JTextField passwordTextField) {
		this.passwordTextField = passwordTextField;
	}
}
