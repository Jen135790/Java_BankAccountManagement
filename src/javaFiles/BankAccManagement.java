/*	**Main start up file**
 *  BankAccManagement.java
 *  Author: Jeanis Sananikone
 *  [Tasks]
 *  - Existing users must login with their Username and Password
 *  	* If they don't exist in the database, then login will be rejected
 *  		and they may choose to reset their password.
 *  	* Users must provide their username and email to reset their password
 *  	* If the user provided a username or email that doesn't exist, then they will
 *  		be denied. The only other option is to register.
 *  - New users can register.
 *  	* They must enter their full name, email, password, confirm that password, and 
 *  		a username to login.
 *  - Once users are in their account, they can make deposits and withdrawals from their
 *  	accounts.
 *  	* Users can't withdrawal more than their current balance.
 *  	* Both Deposit and Withdrawal windows will handle error checking to
 *  		validate input.
 *  - All deposits and withdrawals will be recorded in the transactions list on the
 *  	account window.
 *  	* Transactions lists will have the date of transaction, the amount, and the type (D or W)
 *  - Users can change their email or passwords once they are logged into their account
 *  - Users can log off, allowing another user to log in.
 *  - All data is kept in a database.
 */

import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

public class BankAccManagement extends JFrame implements ActionListener{
	//ICON:
	private ImageIcon iconImg = new ImageIcon(this.getClass().getClassLoader().getResource("TeamGrayLogo.png"));
	
	//Customer:
	Customer customer = new Customer();
	
	//Welcome COMPONENTS:
	private JLabel lblFName = new JLabel(" First Name ");
	private JLabel lblLName = new JLabel(" Last Name");
	
	//Account Details COMPONENTS:
	private JLabel lblBalLabel = new JLabel("Total Balance: "), lblBalance = new JLabel("$ 0.00"), lblEmail = new JLabel("Email: ");
	private JTextField txtEmail = new JTextField(30);
	private JLabel changeEmailErr = new JLabel("* Invalid Email!");
	private JButton btnChangeEmail = new JButton("Change Email"), btnChangePass = new JButton("Change Password");
	 
	//Transaction Details COMPONENTS
	//String[] actionTypes = {"All", "Deposits", "Withdrawals"};
	//private JComboBox<String> cmbTransaction = new JComboBox<String>(actionTypes);
	private JTextArea transactionList = new JTextArea(20, 50);
	private JScrollPane scrollList = new JScrollPane(transactionList);
	
	//Menu Bar:
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu actionMenu = new JMenu("Actions");
	private JMenuItem jmiLogout = new JMenuItem("Log Out");
	private JMenuItem jmiExit = new JMenuItem("Exit");
	private JMenuItem jmiDeposit = new JMenuItem("Deposit Money");
	private JMenuItem jmiWithdrawal = new JMenuItem("Withdrawal Money");
	
	private Date today;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	
	public BankAccManagement(){
		super("Bank Account Management");
		this.setIconImage(iconImg.getImage());
		this.setLayout(new BorderLayout());
		
		//SHOW LOGIN DIALOG FIRST:
		showLogin();
		this.setVisible(true);
		
		//Set start location:
		//Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Menu Bar:
		menuBar.add(fileMenu);
		menuBar.add(actionMenu);
		fileMenu.add(jmiLogout);
		fileMenu.add(jmiExit);
		actionMenu.add(jmiDeposit);
		actionMenu.add(jmiWithdrawal);
		
		//Set up panels:
		JPanel welcomePan = new JPanel();
		JPanel accContents = new JPanel();
		welcomePan.setLayout(new FlowLayout(FlowLayout.CENTER));
		welcomePan.setPreferredSize(new Dimension(400, 40));
		accContents.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		//Welcome Pan Components:
		JLabel lblWelcome = new JLabel("Welcome ");
		lblWelcome.setFont(new Font("Serif", Font.PLAIN, 18));
		lblFName.setFont(new Font("Serif", Font.PLAIN, 18));
		lblLName.setFont(new Font("Serif", Font.PLAIN, 18));
		welcomePan.add(lblWelcome);
		welcomePan.add(lblFName);
		welcomePan.add(lblLName);
		
		//Account components:
		JPanel emailPan = new JPanel();
		JPanel accButtonsPan = new JPanel();
		JPanel accBalPan = new JPanel();
		JPanel accCenterPan = new JPanel();
		accCenterPan.setLayout(new BoxLayout(accCenterPan, BoxLayout.Y_AXIS));
		accBalPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		accButtonsPan.setLayout(new FlowLayout(FlowLayout.LEFT));
		emailPan.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		emailPan.add(lblEmail); emailPan.add(txtEmail); emailPan.add(changeEmailErr); txtEmail.setEditable(false);
		changeEmailErr.setVisible(false);
		accButtonsPan.add(btnChangeEmail); 
		accButtonsPan.add(btnChangePass);
		accBalPan.add(lblBalLabel); 
		accBalPan.add(lblBalance);
		accContents.setBorder(new EmptyBorder(50, 20, 50, 20));
		accCenterPan.add(accBalPan);
		accCenterPan.add(emailPan);
		accCenterPan.add(accButtonsPan);
		accContents.add(accCenterPan);
		lblBalLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblBalance.setFont(new Font("SansSerif", Font.BOLD, 18));

		//Transaction components:
		transactionList.setEditable(false);
		JPanel transactionPan = new JPanel();
		JPanel listPan = new JPanel();
		JPanel comboBoxPan = new JPanel();
		listPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		scrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		comboBoxPan.setLayout(new FlowLayout(FlowLayout.LEFT));
		transactionPan.setLayout(new BoxLayout(transactionPan, BoxLayout.Y_AXIS));
		transactionPan.setBorder(new EmptyBorder(20, 20, 20, 20));
		listPan.add(scrollList);
		transactionPan.add(comboBoxPan);
		transactionPan.add(listPan);
		
		//MainContents Components:
		JTabbedPane tabs =  new JTabbedPane();
		tabs.add("Account Details", accContents);
		tabs.add("Transactions", transactionPan);
		
		this.setJMenuBar(menuBar);
		this.add(welcomePan, BorderLayout.NORTH);
		this.add(tabs, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		
		btnChangeEmail.addActionListener(this);
		btnChangePass.addActionListener(this);
		jmiLogout.addActionListener(this);
		jmiExit.addActionListener(this);
		jmiDeposit.addActionListener(this);
		jmiWithdrawal.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnChangeEmail){
			if(!txtEmail.isEditable()){
				txtEmail.setEditable(true);
			}
			else{
				//Use Regular Expressions to validate email addresses:
				Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				Matcher m = p.matcher(txtEmail.getText());
				boolean validEmail = m.matches();
				if(validEmail){	//IF VALID, call changeEmail
					changeEmail();
					changeEmailErr.setVisible(false);
					txtEmail.setEditable(false);
				}
				else{	//ELSE, display error!
					changeEmailErr.setVisible(true);
				}
			}
		}
		else if(e.getSource() == btnChangePass){ 	//Change Password BUTTON
			changePassword();
		}
		else if(e.getSource() == jmiLogout){ 	//LOGOUT FILE MENU OPTION
			transactionList.setText("");
			showLogin();
			this.setVisible(true);
		}
		else if(e.getSource() == jmiExit){	//EXIT FILE MENU OPTION
			System.exit(0);
		}
		else if(e.getSource() == jmiDeposit){
			makeDeposit();
		}
		else if(e.getSource() == jmiWithdrawal){
			makeWithdrawal();
		}
	}
	
	void showLogin(){
		this.setVisible(false);
		Login loginDialog = new Login(this);
		loginDialog.setLocationRelativeTo(this);
		loginDialog.setVisible(true);
		loginDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		if(!loginDialog.getLoginStatus()){
			System.exit(0);
		}
		else{
			
			customer = loginDialog.getCustomer();
			lblFName.setText(customer.getFName());
			lblLName.setText(customer.getLName());
			txtEmail.setText(customer.getEmail());
			lblBalance.setText("$" + customer.getBalance());
			
			//Get all the vectors:
			Vector<String> transDates = customer.getTransDate();
			Vector<Character> transTypes = customer.getTransType();
			Vector<Double> transAmounts = customer.getTransAmount();
			Vector<Double> transBalances = customer.getTransBalance();
			
			transactionList.append("Date\tType\tAmount\tBalanceAfter\n");
			for(int i = 0; i < customer.getNumOfTransactions(); i++ ){
				fillTransactionList(transDates.get(i).toString(), transTypes.get(i), transAmounts.get(i), transBalances.get(i));
			}
		}
	}
	
	public void fillTransactionList(String date, char type, double amount, double balance){
		transactionList.append(date + "\t" + type+ "\t" + String.format("$%.2f", amount) + "\t" + String.format("$%.2f", balance) + "\n");
	}
	
	public void changeEmail(){
		try{
			DBConnect DB = new DBConnect();
			Connection conn = DB.connect();
			
			String updateQuery = "UPDATE Customer SET Email = \'" + txtEmail.getText() + "\' WHERE CustomerID = " + Integer.parseInt(customer.getCustomerID());
			
			Statement updateEmail = conn.createStatement();
			updateEmail.executeQuery(updateQuery);
			
			updateEmail.close();
			conn.close();
			DB.closeConn();
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void changePassword(){
		ChangePassword changePassDialog = new ChangePassword(this, Integer.parseInt(customer.getCustomerID()));
		changePassDialog.setVisible(true);
		changePassDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	public void makeDeposit(){
		Deposit depositDialog = new Deposit(this);
		depositDialog.setVisible(true);
		depositDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		if(depositDialog.getSuccessStatus()){
			double depositAmount = depositDialog.getDeposit();
			double totalBal;
			if(customer.getTransBalance().size() == 0){
				totalBal = depositAmount;
			}
			else{
				totalBal = (customer.getTransBalance().get(customer.getTransBalance().size() - 1) + depositAmount);
			}
			
			try{
				DBConnect DB = new DBConnect();
				Connection conn = DB.connect();
				
				String insertQuery = "INSERT INTO Transactions(AccountID, Type, Amount, BalanceAfter) VALUES(\'" + customer.getAccountNum() + "\', \'D\', " + depositAmount + ", " + totalBal + ")";
				String updateTotalBal = "UPDATE Accounts SET TotalBalance = " + totalBal + " WHERE AccountID = " + customer.getAccountNum();
				Statement insertDeposit = conn.createStatement();
				insertDeposit.executeUpdate(insertQuery);
				insertDeposit.executeUpdate(updateTotalBal);
				
				today = new Date();
				
				lblBalance.setText(String.format("$%.2f", totalBal));
				fillTransactionList(dateFormat.format(today), 'D', depositAmount, totalBal);
				
				customer.addTransDate(dateFormat.format(today));
				customer.addTransType('D');
				customer.addTransAmount(depositAmount);
				customer.addTransBalance(totalBal);
				
				insertDeposit.close();
				conn.close();
				DB.closeConn();
			}
			catch(SQLException e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void makeWithdrawal(){
		Withdrawal withdrawalDialog = new Withdrawal(this, customer.transBalance.get(customer.transBalance.size() - 1));
		withdrawalDialog.setVisible(true);
		withdrawalDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		if(withdrawalDialog.getSuccessStatus()){
			double withdrawalAmount = withdrawalDialog.getWithdrawal();
			double totalBal = withdrawalDialog.getTotalBal();
			
			
			try{
				DBConnect DB = new DBConnect();
				Connection conn = DB.connect();
				
				String insertQuery = "INSERT INTO Transactions(AccountID, Type, Amount, BalanceAfter) VALUES(\'" + customer.getAccountNum() + "\', \'W\', " + withdrawalAmount + ", " + totalBal + ")";
				String updateTotalBal = "UPDATE Accounts SET TotalBalance = " + totalBal + " WHERE AccountID = " + customer.getAccountNum();
				Statement insertDeposit = conn.createStatement();
				insertDeposit.executeUpdate(insertQuery);
				insertDeposit.executeUpdate(updateTotalBal);
				
				today = new Date();
				
				lblBalance.setText(String.format("$%.2f", totalBal));
				fillTransactionList(dateFormat.format(today), 'W', withdrawalAmount, totalBal);
				
				customer.addTransDate(dateFormat.format(today));
				customer.addTransType('W');
				customer.addTransAmount(withdrawalAmount);
				customer.addTransBalance(totalBal);
				
				insertDeposit.close();
				conn.close();
				DB.closeConn();
			}
			catch(SQLException e){
				System.out.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		BankAccManagement GUI = new BankAccManagement();
		GUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

}
