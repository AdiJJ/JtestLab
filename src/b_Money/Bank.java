package b_Money;

import java.util.Hashtable;

public class Bank {
	private Hashtable<String, Account> accountlist = new Hashtable<String, Account>();
	private String name;
	private Currency currency;
	
	/**
	 * New Bank
	 * @param name Name of this bank
	 * @param currency Base currency of this bank (If this is a Swedish bank, this might be a currency class representing SEK)
	 */
	Bank(String name, Currency currency) {
		this.name = name;
		this.currency = currency;
	}
	
	/**
	 * Method getting the name of this bank
	 * @return Name of this bank
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method getting the Currency of this bank
	 * @return The Currency of this bank
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Open an account at this bank. Method puts a new account data in a hashtable, distinguished by the account id
	 * @param accountid The ID of the account
	 * @throws AccountExistsException If the account already exists
	 */
	public void openAccount(String accountid) throws AccountExistsException {
		if (accountlist.containsKey(accountid)) {
			throw new AccountExistsException();
		}
		else {
			/**
			 * method get doesn't add anything to the hashtable, it only retrieves value mapped by the key
			 * Because of that no account has been opened and AccountExistsException was never thrown
			 * so the test never failed.
			 */
			//accountlist.get(accountid);
			accountlist.put(accountid,new Account(accountid,this.currency));
		}
	}
	
	/**
	 * Deposit money to an account. Method adds Money (in this bank's currency) to the Account specified by the id in parameters.
	 * @param accountid Account to deposit to
	 * @param money Money to deposit.
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public void deposit(String accountid, Money money) throws AccountDoesNotExistException {
		/**
		 * Wrong if statement. It makes it so the AccountDoesNotExist error will be thrown when the account exist
		 * and proceed with depositing money when account doesn't exist.
		 */
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			account.deposit(money);
		}
	}
	
	/**
	 * Withdraw money from an account.Method subdivides Money (in this bank's currency) from the Account specified by the id in parameters.
	 * @param accountid Account to withdraw from
	 * @param money Money to withdraw
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public void withdraw(String accountid, Money money) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			/**
			 * Instead of withdrawing money from the account, the method deposits more money into it.
			 * The balance in the test was bigger instead of smaller as expected
			 */
			account.withdraw(money);
		}
	}
	
	/**
	 * Get the balance of an account, which is amount of money currently on the account
	 * @param accountid Account to get balance from
	 * @return Balance of the account
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public Integer getBalance(String accountid) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			/**
			 * .getAmount() is unnecessary and creates an error as the getBalance function already returns the amount of Money
			 * not the Money itself.
			 */
			return accountlist.get(accountid).getBalance();
		}
	}

	/**
	 * Transfer money between two accounts. Adds money to the account specified by toaccount and tobank id
	 * and removes this amount from the account specified by fromaccount id
	 * @param fromaccount Id of account to deduct from in this Bank
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	public void transfer(String fromaccount, Bank tobank, String toaccount, Money amount) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(fromaccount) || !tobank.accountlist.containsKey(toaccount)) {
			throw new AccountDoesNotExistException();
		}
		else {
			accountlist.get(fromaccount).withdraw(amount);
			tobank.accountlist.get(toaccount).deposit(amount);
		}		
	}

	/**
	 * Transfer money between two accounts on the same bank. Adds money to the account specified by toaccount id
	 * and removes this amount from the account specified by fromaccount id
	 * @param fromaccount Id of account to deduct from
	 * @param toaccount Id of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	public void transfer(String fromaccount, String toaccount, Money amount) throws AccountDoesNotExistException {
		/**
		 * The method transfers money from the same account to the same account instead of between two different
		 * The balance in the Test stayed the same. This is because the parameters where set up incorrectly.
		 * fromaccount->toaccount
		 */
		transfer(fromaccount, this, toaccount, amount);
	}

	/**
	 * Add a timed payment to the Accounts timedpayements hashtable
	 * @param accountid Id of account to deduct from in this Bank
	 * @param payid Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 */
	public void addTimedPayment(String accountid, String payid, Integer interval, Integer next, Money amount, Bank tobank, String toaccount) {
		Account account = accountlist.get(accountid);
		account.addTimedPayment(payid, interval, next, amount, tobank, toaccount);
	}

	/**
	 * Remove a timed payment from the Accounts timedpayements hashtable
	 * @param accountid Id of account to remove timed payment from
	 * @param id Id of timed payment
	 */
	public void removeTimedPayment(String accountid, String id) {
		Account account = accountlist.get(accountid);
		account.removeTimedPayment(id);
	}
	
	/**
	 * A time unit passes in the system
	 */
	public void tick() throws AccountDoesNotExistException {
		for (Account account : accountlist.values()) {
			account.tick();
		}
	}	
}
