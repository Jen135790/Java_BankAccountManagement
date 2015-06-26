import java.util.Vector;


public class Customer {
	private String accountNum;
	private String customerID;
	private String FName;
	private String LName;
	private String Email;
	private String totalBal;
	public Vector<String> transDate = new Vector<String>();
	public Vector<Character> transType = new Vector<Character>();
	public Vector<Double> transAmount = new Vector<Double>();
	public Vector<Double> transBalance = new Vector<Double>();
	
	public Customer(){
		FName = "First Name";
		LName = "Last Name";
		Email = "Email@email.email";
		totalBal = "$00.00";
	}
	
	public Customer(String fn, String ln, String Email, String balance){
		FName = fn;
		LName = ln;
		this.Email = Email;
		totalBal = balance.substring(0, balance.length() - 2);
	}
	
	//COPY CONSTRUCTOR:
	public Customer(Customer c){
		FName = c.FName;
		LName = c.LName;
		Email = c.Email;
		totalBal = c.totalBal;
	}
	public void setAccountNum(String acc){
		accountNum = acc;
	}
	public void setCustomerID(String id){
		customerID = id;
	}
	public void addTransDate(String date){
		transDate.addElement(date);
	}
	
	public void addTransType(char type){
		transType.addElement(type);
	}
	
	public void addTransAmount(double amount){
		transAmount.addElement(amount);
	}
	
	public void addTransBalance(double balance){
		transBalance.addElement(balance);
	}
	
	public String getAccountNum(){
		return accountNum;
	}
	
	public String getCustomerID(){
		return customerID;
	}
	
	public String getFName(){
		return FName;
	}
	
	public String getLName(){
		return LName;
	}
	
	public String getEmail(){
		return Email;
	}
	
	public String getBalance(){
		
		return totalBal;
	}
	
	public Vector<String> getTransDate(){
		return transDate;
	}
	
	public Vector<Character> getTransType(){
		return transType;
	}
	
	public Vector<Double> getTransAmount(){
		return transAmount;
	}
	
	public Vector<Double> getTransBalance(){
		return transBalance;
	}
	
	public int getNumOfTransactions(){
		return transDate.size();
	}
}
