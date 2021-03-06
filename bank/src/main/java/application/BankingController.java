package application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import database.AccountDao;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class BankingController {
	private JsonObject json;
	private JsonParser parser = new JsonParser();
	private AccountDao accountDao = new AccountDao();
	private JsonObject response;
	

	@RequestMapping( value = "/account", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public String createAccount(@RequestBody String body) {
		json  = (JsonObject) parser.parse(body);
		response = new JsonObject();
		
		if(json.get("funds").getAsInt() < 1) {
			response.addProperty("message", "Accounts can't be opened without adequate funds");
		} else {
			boolean created = accountDao.createAccount(
					json.get("accountID").getAsInt(),
					json.get("firstname").getAsString(),
					json.get("lastname").getAsString(),
					json.get("funds").getAsInt());
			if(created) {
				response.addProperty("message", "Account was successfully created");
			} else {
				response.addProperty("message", "Account creation failed");
			}
		}
		
		return response.toString();
	}
	
	@RequestMapping(value = "{accountID}/balance", method = RequestMethod.GET)
	public String showBalance(@PathVariable("accountID")  int accountID) {
		response = new JsonObject();		
		response.addProperty("balance", accountDao.getFunds(accountID));
		
		return response.toString();
	}
	
	@RequestMapping(value = "transfer", method = RequestMethod.POST)
	public String transferFunds(@RequestBody String body) {
		json = (JsonObject) parser.parse(body);
		response = new JsonObject();
		
		if(accountDao.transferFunds(json.get("accountFrom").getAsInt(), json.get("accountTo").getAsInt(), json.get("amount").getAsInt())) {
			accountDao.createTransactions(json.get("accountTo").getAsInt(), json.get("accountFrom").getAsInt(), json.get("amount").getAsInt());
			response.addProperty("message", "Transfer has been successful");
		} else {
			response.addProperty("message", "Transfer was unsuccessful");
		}
		return response.toString();
	}
	
	@RequestMapping(value = "{accountID}/transactions", method = RequestMethod.GET)
	public String showTransactions(@PathVariable("accountID") int accountID ) {
		
		
		return accountDao.getTransactions(accountID);
	}
}