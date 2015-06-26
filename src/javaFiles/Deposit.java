//Deposit.java

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Deposit extends JDialog implements ActionListener{

	private JButton btnOk = new JButton("Ok"), btnCancel = new JButton("Cancel");
	private JLabel lblDeposit = new JLabel("Enter amount to deposit: $"), depositErr = new JLabel("");
	private JTextField txtDeposit = new JTextField(10);
	private double depositAmount = 0;
	private boolean success = false; 
	
	public Deposit(JFrame parent){
		super(parent, "Make a Deposit", true);
		
		JPanel mainPan = new JPanel();
		JPanel inputPan = new JPanel();
		JPanel buttonPan = new JPanel();
		mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.Y_AXIS));
		inputPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		depositErr.setFont(new Font("SansSerif", Font.BOLD, 16));
		depositErr.setForeground(Color.RED);
		
		inputPan.add(lblDeposit);
		inputPan.add(txtDeposit);
		inputPan.add(depositErr);
		buttonPan.add(btnOk);
		buttonPan.add(btnCancel);
		mainPan.add(inputPan);
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
			if(txtDeposit.getText().equals("")){
				depositErr.setText("*");
			}
			else{
				try{
					depositAmount = Double.parseDouble(txtDeposit.getText());
					
					if(depositAmount < .01){
						JOptionPane.showMessageDialog(null, "Amount needs to be greater than $0.00!");
					}
					else{
						success = true;
						JOptionPane.showMessageDialog(null, String.format("Successfully deposited $%.2f to your account!", depositAmount));
						this.dispose();
					}
				}
				catch(NumberFormatException err){
					JOptionPane.showMessageDialog(null, "Invalid amount entered!");
				}
			}
		}
		else if(e.getSource() == btnCancel){
			this.dispose();
		}
	}
	
	public double getDeposit(){
		return depositAmount;
	}
	
	public boolean getSuccessStatus(){
		return success;
	}
}
