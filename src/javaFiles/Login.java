//Login.java

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class Login extends JDialog implements ActionListener{
	//COMPONENTS:
	private JButton btnLogin = new JButton("Login"), btnForgot = new JButton("Forgot User Name/Password"), btnRegister = new JButton("Register");
	private JTextField txtLogin = new JTextField(20); 
	private JPasswordField	txtPassword = new JPasswordField(20);
	private JLabel lblLogin = new JLabel("User Name: "), lblPassword = new JLabel("Password: ");
	private JLabel usernameErr = new JLabel(""), passwordErr = new JLabel(""), loginErr = new JLabel("");
	private Customer c = new Customer();
	
	private boolean loginSuccess = false;
	
	public Login(JFrame parent){
		super(parent, "Checking Bank Account :: Login", true);
		this.setLayout(new GridLayout(4, 1));
		
		usernameErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		usernameErr.setForeground(Color.RED);
		passwordErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		passwordErr.setForeground(Color.RED);
		loginErr.setFont(new Font("SanSerif", Font.BOLD, 18));
		loginErr.setForeground(Color.RED);
		
		JPanel usernamePan = new JPanel();
		JPanel passwordPan = new JPanel();
		JPanel buttonPan = new JPanel();
		usernamePan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		passwordPan.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		usernamePan.add(lblLogin);
		usernamePan.add(txtLogin);
		usernamePan.add(usernameErr);
		passwordPan.add(lblPassword);
		passwordPan.add(txtPassword);
		passwordPan.add(passwordErr);
		buttonPan.add(btnLogin);
		buttonPan.add(btnRegister);
		buttonPan.add(btnForgot);
		
		this.add(usernamePan);
		this.add(passwordPan);
		this.add(buttonPan);
		this.add(loginErr);
		this.pack();
		this.setLocationRelativeTo(parent);
		
		//ActionListeners:
		btnLogin.addActionListener(this);
		btnRegister.addActionListener(this);
		btnForgot.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnLogin){ 	//LOGIN BUTTON *******************************
			if(txtPassword.getPassword().length == 0 || txtLogin.getText().equals("")){
				if(txtPassword.getPassword().length == 0){
					passwordErr.setText("*");
					loginErr.setText("Login Failed! Enter Password!");
				}
				if(txtLogin.getText().equals("")){
					usernameErr.setText("*");
					loginErr.setText("Login Failed! Enter User Name!");
				}
			}
			else{
				loginErr.setText("Success!");
				String username = txtLogin.getText();
				char[] password = txtPassword.getPassword();
				StringBuffer passInAscii = new StringBuffer();
				
				for(int i = 0; i < password.length; i++){
					passInAscii.append((int)password[i]);
				}
				
				//SQL:
				try{
					//Connect to Database:
					DBConnect DB = new DBConnect();
					Connection conn = DB.connect();
					
					Statement s = conn.createStatement();
					ResultSet results = s.executeQuery("SELECT * FROM Customer INNER JOIN Accounts ON Customer.CustomerID = Accounts.CustomerID WHERE Username = \'" + username + "\' AND Password = \'" + passInAscii.toString() + "\'");
					
					if(results.next()){
						setCustomer(results.getString(4), results.getString(5), results.getString(6), results.getString(10));
						c.setCustomerID(results.getString(1));
						c.setAccountNum(results.getString(7));
						System.out.println(c.getAccountNum() + " " + c.getCustomerID());
						ResultSet tranactionResults = s.executeQuery("SELECT Date, Transactions.Type, Amount, BalanceAfter FROM Accounts INNER JOIN Transactions ON Accounts.AccountID = Transactions.AccountID WHERE Accounts.AccountID = " + c.getAccountNum());
						int rows = tranactionResults.getRow();
						System.out.println("Rows: " + rows);
						while(tranactionResults.next()){
							c.addTransDate(tranactionResults.getDate(1).toString());
							c.addTransType(tranactionResults.getString(2).charAt(0));
							c.addTransAmount(tranactionResults.getDouble(3));
							c.addTransBalance(tranactionResults.getDouble(4));
						}
						
						loginSuccess = true;
						dispose();
					}
					else{
						loginErr.setText("User Name/Password doesn't exist!");
					}
					
					results.close();
					conn.close();
					DB.closeConn();
				}
				catch(SQLException err){
					System.out.println(err.getMessage());
				}
			}
		}
		else if(e.getSource() == btnRegister){
			Register registerDialog = new Register((JFrame)this.getParent());
			registerDialog.setVisible(true);
			registerDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
		else if(e.getSource() == btnForgot){
			ForgotPassword forgotPassDialog = new ForgotPassword((JFrame)this.getParent());
			forgotPassDialog.setVisible(true);
			forgotPassDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}
	
	public boolean getLoginStatus(){
		return loginSuccess;
	}
	
	public void setCustomer(String fn, String ln, String email, String balance){
		c = new Customer(fn, ln, email, balance);
	}
	
	public Customer getCustomer(){
		return c;
	}
	
}
