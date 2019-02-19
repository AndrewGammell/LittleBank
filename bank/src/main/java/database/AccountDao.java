package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import connector.DatabaseConnector;


public class AccountDao {
	
	private Connection connection = DatabaseConnector.getConnection();
	private Statement stmt;
	private JsonArray jarray;
	private JsonObject obj;
	private ResultSet rs;
	
	public boolean createAccount(int accountID, String firstname, String lastname, int funds) {
		
		try {
			stmt = connection.createStatement();
			String account = String.format("INSERT INTO ACCOUNTS VALUES ('%d','%s','%s','%d')", accountID,firstname,lastname,funds);
			stmt.executeUpdate(account);
			return true;
		} catch (Exception e) {
			e.getStackTrace();
			return false;
		}
	}
	
	public boolean transferFunds(int accountFrom, int accountTo, int transferAmount) {
		
		if(getFunds(accountFrom) < transferAmount) {
			return false;
		} else {
			updateFunds(accountFrom, getFunds(accountFrom) - transferAmount);		
			updateFunds(accountTo, getFunds(accountTo) + transferAmount);
			return true;
		}
	}
	

	public int getFunds(int account) {
		
		String checkFunds = "SELECT funds FROM ACCOUNTS WHERE accountID = " + account;
		int funds = -1;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(checkFunds);
			rs.next();
			funds = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			
		} 

		return funds;
	}
	
	private void updateFunds(int account, int funds) {

		String checkFunds = "UPDATE ACCOUNTS SET funds = '%d' WHERE accountID = %s";
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(String.format(checkFunds, funds, account));
			
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	public boolean createTransactions(int accountTo, int accountFrom, int funds) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			String account = String.format("INSERT INTO TRANSACTIONS VALUES ('%d','%s','%s','%tF')", accountFrom, accountTo, funds, LocalDate.now());
			stmt.executeUpdate(account);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getTransactions(int accountID) {
		jarray = new JsonArray();
		
		try {
			stmt = connection.createStatement();
			String getTransactions = String.format("SELECT * FROM TRANSACTIONS WHERE accountFrom = %d OR accountTo = %d",accountID, accountID);
			rs = stmt.executeQuery(getTransactions);
			
			while(rs.next()) {
				obj = new JsonObject();
				obj.addProperty("accountFrom", rs.getInt(1));
				obj.addProperty("accountTo", rs.getInt(2));
				obj.addProperty("amount", rs.getInt(3));
				obj.addProperty("date", rs.getDate(4, Calendar.getInstance()).toString());
				
				jarray.add(obj);
			}
			return jarray.toString();
		} catch (Exception e) {
			e.printStackTrace();
			obj = new JsonObject();
			obj.addProperty("message", "Error occured getting transactions");
			return obj.toString() ;
		}		
	}
	
}
