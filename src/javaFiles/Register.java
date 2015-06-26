//Register.java

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends JDialog implements ActionListener{
	
	private JButton btnRegister = new JButton("Register");
	private JButton btnCancel = new JButton("Cancel");
	private JLabel lblusername = new JLabel("User Name: "), lblPassword = new JLabel("Password: "), lblConfirmPassword = new JLabel("Confirm Password: "),
					lblFName = new JLabel("First Name: "), lblLName = new JLabel("Last Name: "), lblEmail = new JLabel("Email: ");
	private JTextField txtusername = new JTextField(20), txtFName = new JTextField(25), txtLName = new JTextField(25), txtEmail = new JTextField(25);
	private JPasswordField txtPassword = new JPasswordField(20), txtConfirmPassword = new JPasswordField(20);
	private JLabel usernameErr = new JLabel("*"), passwordErr = new JLabel("*"), confirmPasswordErr = new JLabel("*"), FNameErr = new JLabel("*"), LNameErr = new JLabel("*"), emailErr = new JLabel("*");
	private String username, FName, LName, email;
	private boolean success = true;
	private boolean validEmail;
	private boolean isSame;
	
	public Register(JFrame parent){
		super(parent, "Checking Bank Account :: Register", true);
		
		//Declaring, Initializing, and setting Panel Layouts
		JPanel mainPan = new JPanel();
		JPanel usernamePan = new JPanel();
		JPanel passwordPan = new JPanel();
		JPanel confirmPasswordPan = new JPanel();
		JPanel fNamePan = new JPanel();
		JPanel lNamePan = new JPanel();
		JPanel emailPan = new JPanel();
		JPanel buttonPan = new JPanel();
		mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.Y_AXIS));
		mainPan.setBorder(new EmptyBorder(15, 25, 15, 25));
		usernamePan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		passwordPan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		confirmPasswordPan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		fNamePan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		lNamePan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		emailPan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		usernameErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		passwordErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		confirmPasswordErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		FNameErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		LNameErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		emailErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		
		usernameErr.setForeground(Color.RED);
		passwordErr.setForeground(Color.RED);
		confirmPasswordErr.setForeground(Color.RED);
		FNameErr.setForeground(Color.RED);
		LNameErr.setForeground(Color.RED);
		emailErr.setForeground(Color.RED);
		
		usernamePan.add(lblusername);
		usernamePan.add(txtusername);
		usernamePan.add(usernameErr);
		passwordPan.add(lblPassword);
		passwordPan.add(txtPassword);
		passwordPan.add(passwordErr);
		confirmPasswordPan.add(lblConfirmPassword);
		confirmPasswordPan.add(txtConfirmPassword);
		confirmPasswordPan.add(confirmPasswordErr);
		fNamePan.add(lblFName);
		fNamePan.add(txtFName);
		fNamePan.add(FNameErr);
		lNamePan.add(lblLName);
		lNamePan.add(txtLName);
		lNamePan.add(LNameErr);
		emailPan.add(lblEmail);
		emailPan.add(txtEmail);
		emailPan.add(emailErr);
		buttonPan.add(btnRegister);
		buttonPan.add(btnCancel);
		
		mainPan.add(fNamePan);
		mainPan.add(lNamePan);
		mainPan.add(emailPan);
		mainPan.add(usernamePan);
		mainPan.add(passwordPan);
		mainPan.add(confirmPasswordPan);
		mainPan.add(buttonPan);
		this.add(mainPan);
		this.pack();
		this.setLocationRelativeTo(parent);
		
		btnRegister.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnRegister){	//REGISTER BUTTON
			//Check if any of the textboxes are empty:
			if(txtusername.getText().equals("") || txtEmail.getText().equals("") || txtFName.getText().equals("")
			|| txtLName.getText().equals("") || txtPassword.getPassword().length == 0 || txtConfirmPassword.getPassword().length == 0){
				if(txtConfirmPassword.getPassword().length == 0){
					confirmPasswordErr.setVisible(true);
				}
				else{
					confirmPasswordErr.setVisible(false);
				}
				
				if(txtPassword.getPassword().length == 0){
					passwordErr.setVisible(true);
				}
				else{
					passwordErr.setVisible(false);
				}
				
				if(txtusername.getText().equals("")){
					usernameErr.setVisible(true);
				}
				else{
					usernameErr.setVisible(false);
				}
				
				if(txtEmail.getText().equals("")){
					emailErr.setVisible(true);
				}
				else{
					emailErr.setVisible(false);
				}
				
				if(txtLName.getText().equals("")){
					LNameErr.setVisible(true);
				}
				else{
					LNameErr.setVisible(false);
				}
				
				if(txtFName.getText().equals("")){
					FNameErr.setVisible(true);
				}
				else{
					FNameErr.setVisible(false);
				}
			}
			//ELSE:
			else{
				//VALIDATE PASSWORD:
				isSame = true;
				if(txtPassword.getPassword().length != txtConfirmPassword.getPassword().length){
					isSame = false;
				}
				else{
					char j;
					for(int i = 0; i < txtPassword.getPassword().length; i++){
						j = txtPassword.getPassword()[i];
						if(j != txtConfirmPassword.getPassword()[i]){
							isSame = false;
							break;
						}
					}
				}
				
				//VALIDATE EMAIL:
				//Use Regular Expressions to validate email addresses:
				Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				Matcher m = p.matcher(txtEmail.getText());
				validEmail = m.matches();
				if(validEmail){	//IF VALID, call changeEmail
					email = txtEmail.getText();
					emailErr.setVisible(false);
				}
				
				//MAKE SURE BOTH ARE VALID!
				if(!isSame || !validEmail){
					if(!isSame){
						confirmPasswordErr.setVisible(true);
					}
					else{
						passwordErr.setVisible(false);
						confirmPasswordErr.setVisible(false);
					}
					
					if(!validEmail){
						emailErr.setVisible(true);
					}
					else{
						emailErr.setVisible(false);
					}
				}
				else{
					StringBuilder asciiPass = new StringBuilder();
					for(int i = 0; i < txtPassword.getPassword().length; i++){
						asciiPass.append((int)txtPassword.getPassword()[i]);
					}
					username = txtusername.getText();
					FName = txtFName.getText();
					LName = txtLName.getText();
					
					try{
						DBConnect DB = new DBConnect();
						Connection conn = DB.connect();
						
						Statement s = conn.createStatement();
						
						ResultSet results = s.executeQuery("SELECT Username FROM Customer WHERE Username = \'" + username +"\'");
						if(results.next()){
							JOptionPane.showMessageDialog(null, "User Name Already Exists!");
							success = false;
						}
						else{
							s.executeUpdate("INSERT INTO Customer(Username, Password, FirstName, LastName, Email) " + 
										"VALUES ('" + username + "', '" + asciiPass.toString() + "', '" + FName + "', '" + LName + "', '" + email + "')");
							ResultSet getCustomerID = s.executeQuery("SELECT COUNT(*) FROM Customer");
							if(getCustomerID.next()){
								int ID = getCustomerID.getInt(1);
								s.executeUpdate("INSERT INTO Accounts(CustomerID) VALUES(" + ID + ")");
								success = true;
							}
							else{
								System.out.println("ERROR GETTING CUSTOMER ID");
							}
							getCustomerID.close();
						}
						
						s.close();
						results.close();
						conn.close();
						DB.closeConn();
					}
					catch(SQLException err){
						System.out.println(err.getMessage());
					}
					
					if(success){
						JOptionPane.showMessageDialog(null, "Successfully registered your account!");
						this.dispose();
					}
					
				}
			}
		}
		else if(e.getSource() == btnCancel){ 	//CANCEL BUTTON
			this.dispose();
		}
	}
}
