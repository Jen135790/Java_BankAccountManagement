import java.sql.*;
import java.io.*;
import java.nio.file.*;

public class DBConnect {
	private String url1;
	private String url2;
	private Connection conn;
	private String currentDir;
	private String DBFileName = "/BankManagement.mdb";

	public DBConnect(){
		currentDir = System.getProperty("user.dir");
		url1 = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + currentDir + DBFileName;
		url2 = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+ currentDir + DBFileName;
	}
	
	public Connection connect(){
		try{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			try{
				conn = DriverManager.getConnection(url1, "","");
				System.out.println("Successfully Connected in DBConnect using url1");
			}
			catch(SQLException err){
				try {
					conn = DriverManager.getConnection(url2, "","");
					System.out.println("Successfully Connected in DBConnect using url2");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		catch(ClassNotFoundException error){
			System.out.println("DBConnect FAILED");
			error.getMessage();
		}
		return conn;
	}
	
	public void closeConn(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
