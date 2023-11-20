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
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("0",1,4,new Money(5000,SEK),SweBank,"Alice");
		assertTrue(testAccount.timedPaymentExists("0"));
		testAccount.removeTimedPayment("0");
		assertFalse(testAccount.timedPaymentExists("0"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("0", 1, 4, new Money(5000, SEK), SweBank, "Alice");
		testAccount.tick();
		testAccount.tick();
		testAccount.tick();
		assertEquals(9995000,testAccount.getBalance().getAmount(),0);
	}

	@Test
	public void testAddWithdraw() {
		testAccount.deposit(new Money(300, SEK));
		assertEquals(10000300,testAccount.getBalance().getAmount(),0);
		testAccount.withdraw(new Money(500, SEK));
		assertEquals(9999800,testAccount.getBalance().getAmount(),0);
	}
	
	@Test
	public void testGetBalance() {
		assertEquals(10000000,testAccount.getBalance().getAmount(),0);
	}
}
