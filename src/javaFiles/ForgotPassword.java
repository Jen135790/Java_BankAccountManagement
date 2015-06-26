import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;

public class ForgotPassword extends JDialog implements ActionListener{

	private JLabel lblUsername = new JLabel("Enter User Name: "), lblEmail = new JLabel("Enter Email on Account: ");
	private JTextField txtUsername = new JTextField(), txtEmail = new JTextField();
	private JLabel usernameErr = new JLabel(""), emailErr = new JLabel(""), mainErr = new JLabel();
	private JButton btnOk = new JButton("Ok"), btnCancel = new JButton("Cancel");
	
	public ForgotPassword(JFrame parent){
		super(parent, "Checking Bank Account :: Forgot Password", true);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JPanel mainPan = new JPanel();
		JPanel enterPassPan = new JPanel();
		JPanel buttonPan = new JPanel();
		JPanel errorPan = new JPanel();
		enterPassPan.setLayout(new GridLayout(2, 3));
		buttonPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPan.setBorder(new EmptyBorder(15, 25, 15, 25));
		mainPan.setLayout(new GridLayout(3,1));
		errorPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		usernameErr.setFont(new Font("SansSerif", Font.BOLD, 14));
		emailErr.setFont(new Font("SansSerif", Font.BOLD, 14));
		mainErr.setForeground(Color.RED);
		usernameErr.setForeground(Color.RED);
		emailErr.setForeground(Color.RED);
		mainErr.setForeground(Color.RED);
		
		enterPassPan.add(lblUsername);
		enterPassPan.add(txtUsername);
		enterPassPan.add(usernameErr);
		enterPassPan.add(lblEmail);
		enterPassPan.add(txtEmail);
		enterPassPan.add(emailErr);
		buttonPan.add(btnOk);
		buttonPan.add(btnCancel);
		errorPan.add(mainErr);
		
		mainPan.add(enterPassPan);
		mainPan.add(buttonPan);
		mainPan.add(errorPan);
		this.add(mainPan);
		this.pack();
		this.setLocationRelativeTo(parent);
		
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnOk){
			//Check if any textboxes are empty:
			if(txtUsername.getText().equals("") || txtEmail.getText().equals("")){
				if(txtEmail.getText().equals("")){
					emailErr.setText("*");
					emailErr.setVisible(true);
				}
				else{
					emailErr.setVisible(false);
				}
				
				if(txtUsername.getText().equals("")){
					usernameErr.setText("*");
					usernameErr.setVisible(true);
				}
				else{
					usernameErr.setVisible(false);
				}
			}
			else{
				try{
					boolean success;
					DBConnect DB = new DBConnect();
					Connection conn = DB.connect();
					Statement s = conn.createStatement();
					ResultSet results = s.executeQuery("SELECT CustomerID, Username, Email FROM Customer WHERE Username = \'" + txtUsername.getText() + "\' AND Email = \'" + txtEmail.getText() + "\'");
					
					if(results.next()){
						mainErr.setVisible(false);
						ChangePassword changePassDialog = new ChangePassword((JFrame)this.getParent(), results.getInt(1));
						changePassDialog.setVisible(true);
						changePassDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
						
						success = true;
					}
					else{
						mainErr.setText("User Name/Email Doesn't Exist!");
						mainErr.setVisible(true);
						success = false;
					}
					results.close();
					conn.close();
					DB.closeConn();
					
					if(success){
						this.dispose();
					}
				}
				catch(SQLException err){
					System.out.println(err.getMessage());
				}
				
			}
		}
		else if(e.getSource() == btnCancel){
			this.dispose();
		}
	}
}
