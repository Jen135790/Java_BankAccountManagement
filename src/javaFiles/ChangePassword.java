import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.sql.*;

public class ChangePassword extends JDialog implements ActionListener{
	private JLabel lblPassword = new JLabel("Enter New Password: "), lblConfirmPassword = new JLabel("Confirm new Password: ");
	private JPasswordField txtPassword = new JPasswordField(), txtConfirmPassword = new JPasswordField();
	private JLabel passwordErr = new JLabel(""), confirmPasswordErr = new JLabel("");
	private JButton btnOk = new JButton("Ok"), btnCancel = new JButton("Cancel");
	private int customerID;
	
	public ChangePassword(JFrame parent, int ID){
		super(parent, "Checking Bank Account :: Change Password", true);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		customerID = ID;
		
		JPanel mainPan = new JPanel();
		JPanel enterPassPan = new JPanel();
		JPanel buttonPan = new JPanel();
		enterPassPan.setLayout(new GridLayout(2, 3));
		buttonPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPan.setBorder(new EmptyBorder(15, 25, 15, 25));
		mainPan.setLayout(new GridLayout(2,1));
		
		passwordErr.setFont(new Font("SansSerif", Font.BOLD, 14));
		confirmPasswordErr.setFont(new Font("SansSerif", Font.BOLD, 14));
		passwordErr.setForeground(Color.RED);
		confirmPasswordErr.setForeground(Color.RED);
		
		enterPassPan.add(lblPassword);
		enterPassPan.add(txtPassword);
		enterPassPan.add(passwordErr);
		enterPassPan.add(lblConfirmPassword);
		enterPassPan.add(txtConfirmPassword);
		enterPassPan.add(confirmPasswordErr);
		buttonPan.add(btnOk);
		buttonPan.add(btnCancel);
		
		mainPan.add(enterPassPan);
		mainPan.add(buttonPan);
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
			if(txtPassword.getPassword().length == 0 || txtConfirmPassword.getPassword().length == 0){
				if(txtConfirmPassword.getPassword().length == 0){
					confirmPasswordErr.setText("*");
					confirmPasswordErr.setVisible(true);
				}
				else{
					confirmPasswordErr.setVisible(false);
				}
				
				if(txtPassword.getPassword().length == 0){
					passwordErr.setText("*");
					passwordErr.setVisible(true);
				}
				else{
					passwordErr.setVisible(false);
				}
			}
			else{
				boolean isSame = true;
				
				if(txtPassword.getPassword().length != txtConfirmPassword.getPassword().length){
					isSame = false;
				}
				else{
					for(int i = 0; i < txtPassword.getPassword().length; i++){
						char j = txtPassword.getPassword()[i];
						if(j != txtConfirmPassword.getPassword()[i]){
							isSame = false;
							break;
						}
					}
				}
				
				
				if(!isSame){
					confirmPasswordErr.setText("* Doesn't Match!");
					confirmPasswordErr.setVisible(true);
				}
				else{
					confirmPasswordErr.setVisible(false);
					try{
						StringBuilder asciiPass = new StringBuilder();
						for(int i = 0; i < txtPassword.getPassword().length; i++){
							asciiPass.append((int)txtPassword.getPassword()[i]);
						}
						
						DBConnect DB = new DBConnect();
						Connection conn = DB.connect();
						
						String updateQuery = "UPDATE Customer SET Password = \'" + asciiPass + "\' WHERE CustomerID = " + customerID;
						
						Statement updatePass = conn.createStatement();
						updatePass.executeQuery(updateQuery);
						
						updatePass.close();
						conn.close();
						DB.closeConn();
					}
					catch(SQLException err){
						System.out.println(err.getMessage());
					}
					
					JOptionPane.showMessageDialog(null, "Successfully changed your password!");
					this.dispose();
				}
				
			}
		}
		else if(e.getSource() == btnCancel){
			this.dispose();
		}
	}
}
