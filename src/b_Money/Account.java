package b_Money;

import java.util.Hashtable;

public class Account {
	private Money content;
	private Hashtable<String, TimedPayment> timedpayments = new Hashtable<String, TimedPayment>();

	Account(String name, Currency currency) {
		this.content = new Money(0, currency);
	}

	/**
	 * Add a timed payment. Method puts a new timed payment data in a hashtable, distinguished by the timed payment id
	 * @param id Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 */
	public void addTimedPayment(String id, Integer interval, Integer next, Money amount, Bank tobank, String toaccount) {
		TimedPayment tp = new TimedPayment(interval, next, amount, this, tobank, toaccount);
		timedpayments.put(id, tp);
	}
	
	/**
	 * Remove a timed payment. Method removes a timed payment data from a hashtable, distinguished by the timed payment id
	 * @param id Id of timed payment to remove
	 */
	public void removeTimedPayment(String id) {
		timedpayments.remove(id);
	}
	
	/**
	 * Check if a timed payment exists. Method checks if timed payment exists in the hashtable timedpayments
	 * @param id Id of timed payment to check for
	 */
	public boolean timedPaymentExists(String id) {
		return timedpayments.containsKey(id);
	}

	/**
	 * A time unit passes in the system
	 */
	public void tick() {
		for (TimedPayment tp : timedpayments.values()) {
			/**
			 * The timed Payment didn't work correctly, two ticks were made unnecessarily.
			 * Test showed that too many payments have been made in the specified amount of ticks
			 */
			tp.tick();
		}
	}
	
	/**
	 * Deposit money to the account. Adds money to the content of the Account
	 * @param money Money to deposit.
	 */
	public void deposit(Money money) {
		content = content.add(money);
	}
	
	/**
	 * Withdraw money from the account. Removes money from the content of the Account
	 * @param money Money to withdraw.
	 */
	public void withdraw(Money money) {
		content = content.sub(money);
	}

	/**
	 * Get balance (amount of money) of account
	 * @return Amount of Money currently on account
	 */
	public Integer getBalance() {
		/**
		 * return statement tells as that the method should return an Integer not the Money
		 * The assertEquals method in the test couldn't be resolved correctly.
		 */
		return content.getAmount();
	}

	/* Everything below belongs to the private inner class, TimedPayment */
	private class TimedPayment {
		private int interval, next;
		private Account fromaccount;
		private Money amount;
		private Bank tobank;
		private String toaccount;
		
		TimedPayment(Integer interval, Integer next, Money amount, Account fromaccount, Bank tobank, String toaccount) {
			this.interval = interval;
			this.next = next;
			this.amount = amount;
			this.fromaccount = fromaccount;
			this.tobank = tobank;
			this.toaccount = toaccount;
		}

		/* Return value indicates whether or not a transfer was initiated */
		public Boolean tick() {
			if (next == 0) {
				next = interval;

				fromaccount.withdraw(amount);
				try {
					tobank.deposit(toaccount, amount);
				}
				catch (AccountDoesNotExistException e) {
					/* Revert transfer.
					 * In reality, this should probably cause a notification somewhere. */
					fromaccount.deposit(amount);
				}
				return true;
			}
			else {
				next--;
				return false;
			}
		}
	}

}
