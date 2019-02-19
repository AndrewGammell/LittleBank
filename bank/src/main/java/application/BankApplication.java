package application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import database.Database;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		Database.setupTables();
		SpringApplication.run(BankApplication.class, args);
	}

}
