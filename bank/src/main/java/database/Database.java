package database;

import java.sql.Connection;
import java.sql.Statement;

import connector.DatabaseConnector;

public class Database {

	private static Connection connection = DatabaseConnector.getConnection();
	
	public static void setupTables() {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			String accountTable = "CREATE TABLE IF NOT EXISTS ACCOUNTS ("
					+ "accountID INTEGER, "
					+ "firstname VARCHAR(50),"
					+ " lastname VARCHAR(50),"
					+ " funds INTEGER"
					+ ")";
			stmt.executeUpdate(accountTable);
			
			stmt = connection.createStatement();
			String transactionTable = "CREATE TABLE IF NOT EXISTS TRANSACTIONS ("
					+ "accountFrom INTEGER, "
					+ "accountTo INTEGER,"
					+ "funds INTEGER,"
					+ "date date"
					+ ")";
			stmt.executeUpdate(transactionTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
