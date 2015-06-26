import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Withdrawal extends JDialog implements ActionListener{
	private JButton btnOk = new JButton("Ok"), btnCancel = new JButton("Cancel");
	private JLabel lblWithdrawal = new JLabel("Enter amount to withdraw: $"), withdrawalErr = new JLabel("");
	private JTextField txtWithdrawal = new JTextField(10);
	private double withdrawalAmount = 0;
	private double totalBal = 0;
	private boolean success = false; 
	
	public Withdrawal(JFrame parent, double totalBal){
		super(parent, "Make a Deposit", true);
		
		this.totalBal = totalBal;
		
		JPanel mainPan = new JPanel();
		JPanel inputPan = new JPanel();
		JPanel buttonPan = new JPanel();
		mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.Y_AXIS));
		inputPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPan.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		withdrawalErr.setFont(new Font("SansSerif", Font.BOLD, 16));
		withdrawalErr.setForeground(Color.RED);
		
		inputPan.add(lblWithdrawal);
		inputPan.add(txtWithdrawal);
		inputPan.add(withdrawalErr);
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
			if(txtWithdrawal.getText().equals("")){
				withdrawalErr.setText("*");
			}
			else{
				try{
					withdrawalAmount = Double.parseDouble(txtWithdrawal.getText());
					
					if(withdrawalAmount < .01){
						JOptionPane.showMessageDialog(null, "Amount needs to be greater than $0.00!");
					}
					else if(withdrawalAmount > totalBal){
						JOptionPane.showMessageDialog(null, String.format("Amount needs to be less than your total balance ($%.2f)!", totalBal));
					}
					else{
						totalBal -= withdrawalAmount;
						success = true;
						JOptionPane.showMessageDialog(null, String.format("Successfully withdrew $%.2f from your account!", withdrawalAmount));
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
	
	public double getWithdrawal(){
		return withdrawalAmount;
	}
	
	public double getTotalBal(){
		return totalBal;
	}
	
	public boolean getSuccessStatus(){
		return success;
	}
}
