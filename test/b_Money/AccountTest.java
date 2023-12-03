package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		/**
		 * Test passed
		 */
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	/**
	 * Testing if adding, removing and checking if timed payment work correctly
	 */
	@Test
	public void testAddRemoveTimedPayment(){
		testAccount.addTimedPayment("0",1,4,new Money(5000,SEK),SweBank,"Alice");
		/**
		 * Error should be shown here as the timedPayment with this id already exists
		 */
		testAccount.addTimedPayment("0",1,4,new Money(5000,SEK),SweBank,"Alice");
		assertTrue(testAccount.timedPaymentExists("0"));
		testAccount.removeTimedPayment("0");
		assertFalse(testAccount.timedPaymentExists("0"));
		/**
		 * Error should be shown here as the timedPayment with this id does not exist
		 */
		testAccount.removeTimedPayment("0");
	}

	/**
	 * Testing if method does the timed payment correctly
	 * @throws AccountDoesNotExistException
	 */
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException{
		/**
		 * Test failed. The payment was made too many times after the specified amount of ticks.
		 */
		testAccount.addTimedPayment("0", 1, 4, new Money(5000, SEK), SweBank, "Alice");
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		assertEquals(9995000,testAccount.getBalance(),0);
		testAccount.tick();
		testAccount.tick();
		assertEquals(9990000,testAccount.getBalance(),0);
	}

	/**
	 * testing if depositing and withdrawing money to and from account work correctly
	 */
	@Test
	public void testAddWithdraw() {
		/**
		 * Test passed
		 */
		testAccount.deposit(new Money(300, SEK));
		assertEquals(10000300,testAccount.getBalance(),0);
		testAccount.withdraw(new Money(500, SEK));
		assertEquals(9999800,testAccount.getBalance(),0);
	}

	/**
	 * Testing if the method returns correct balance (amount of money)
	 */
	@Test
	public void testGetBalance() {
		/**
		 * Test failed. Method assertEquals couldn't be resolved.
		 */
		assertEquals(10000000,testAccount.getBalance(),0);
	}
}
