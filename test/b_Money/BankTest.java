package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	/**
	 * Testing if getter function returns the name correctly
	 */
	@Test
	public void testGetName() {
		/**
		 * Test passed
		 */
		assertEquals("SweBank",SweBank.getName());
		assertEquals("Nordea",Nordea.getName());
		assertEquals("DanskeBank",DanskeBank.getName());
	}

	/**
	 * Testing if getter function returns the currency correctly
	 */
	@Test
	public void testGetCurrency() {
		/**
		 * Test passed
		 */
		assertSame(SEK,SweBank.getCurrency());
		assertSame(SEK,Nordea.getCurrency());
		assertSame(DKK,DanskeBank.getCurrency());
	}

	/**
	 * Testing if opens the account correctly.
	 * If the account already exists an AccountExistsException should be thrown
	 * @throws AccountExistsException
	 * @throws AccountDoesNotExistException
	 */
	@Test (expected = AccountExistsException.class )
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		/**
		 * The test failed. Account "Alan" should already exist in Nordea Bank however no exception has been thrown,
		 * which means that the method doesn't open accounts correctly
		 */
		Nordea.openAccount("Alan");
		Nordea.openAccount("Alan");
	}

	/**
	 * Testing if the method deposits money to the specified account correctly
	 * @throws AccountDoesNotExistException
	 */
	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		/**
		 * The test failed. Even though Account "Gertrud" exists in this bank the AccountDoesNotExist error is thrown
		 */
		DanskeBank.deposit("Gertrud",new Money(10000,DanskeBank.getCurrency()));
		assertEquals(10000,DanskeBank.getBalance("Gertrud"),0);
		Nordea.deposit("Bob",new Money(5000,Nordea.getCurrency()));
		assertNotEquals(10000,Nordea.getBalance("Bob"),0);
	}

	/**
	 * Testing if the method withdraws money to the specified account correctly
	 * @throws AccountDoesNotExistException
	 */
	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		/**
		 * The test failed. After Withdrawing the money from the account, the balance rose instead of lowering.
		 */
		DanskeBank.deposit("Gertrud",new Money(10000,DanskeBank.getCurrency()));
		DanskeBank.withdraw("Gertrud",new Money(2000,DanskeBank.getCurrency()));
		assertEquals(8000,DanskeBank.getBalance("Gertrud"),0);
	}

	/**
	 * Testing if method returns correct balance
	 * @throws AccountDoesNotExistException
	 */
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		/**
		 * Test failed. couldn't resolve method .getAmount() that was added unnecessarily to getBalance
		 */
		Nordea.deposit("Bob",new Money(5000,Nordea.getCurrency()));
		assertEquals(5000,Nordea.getBalance("Bob"),0);
		assertEquals(0,SweBank.getBalance("Bob"),0);
	}

	/**
	 * Testing if method transfer money between accounts correctly
	 * @throws AccountDoesNotExistException
	 */
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika",new Money(10000,SweBank.getCurrency()));
		SweBank.transfer("Ulrika",DanskeBank,"Gertrud",new Money(5000,DanskeBank.getCurrency()));
		assertEquals(5000,DanskeBank.getBalance("Gertrud"),0);
		assertEquals(3334,SweBank.getBalance("Ulrika"),0);

		/**
		 * The Test failed. The Balances are different from what was expected
		 */
		SweBank.transfer("Ulrika","Bob",new Money(2000,SweBank.getCurrency()));
		assertEquals(1334,SweBank.getBalance("Ulrika"),0);
		assertEquals(2000,SweBank.getBalance("Bob"),0);
		/**
		 * Test passed here, but it shouldn't be able to transfer money from the same account to the same. An error should be shown
		 */
		SweBank.transfer("Ulrika","Ulrika",new Money(2000,SweBank.getCurrency()));
		/**
		 * Test passed here, but it shouldn't be able to transfer 0 money between accounts. An error should be shown
		 */
		SweBank.transfer("Ulrika","Bob",new Money(0,SweBank.getCurrency()));

	}

	/**
	 * Testing if method does the timed payment correctly
	 * @throws AccountDoesNotExistException
	 */
	@Test (expected = AccountDoesNotExistException.class)
	public void testTimedPayment() throws AccountDoesNotExistException{
		SweBank.deposit("Ulrika",new Money(5000,SweBank.getCurrency()));

		SweBank.addTimedPayment("Ulrika","0",1,4,new Money(2000,SweBank.getCurrency()),DanskeBank,"Gertrud");
		SweBank.addTimedPayment("Ulrika","1",1,4,new Money(2000,SweBank.getCurrency()),Nordea,"Bob");
		SweBank.tick();
		SweBank.tick();
		SweBank.tick();
		SweBank.tick();
		SweBank.tick();
		assertEquals(1000,SweBank.getBalance("Ulrika"),0);
		assertEquals(2000,Nordea.getBalance("Bob"),0);
		assertEquals(1500,DanskeBank.getBalance("Gertrud"),0);

		SweBank.removeTimedPayment("Ulrika","0");
		SweBank.tick();
		SweBank.tick();
		assertEquals(-1000,SweBank.getBalance("Ulrika"),0);
		assertEquals(4000,Nordea.getBalance("Bob"),0);
		assertEquals(1500,DanskeBank.getBalance("Gertrud"),0);
		/**
		 * Test failed. the addTimedPayment should invoke an AccountDoesNotExistException because there is no account called "alan"
		 */
		SweBank.addTimedPayment("alan","2",1,4,new Money(2000,SweBank.getCurrency()),Nordea,"Bob");
		SweBank.removeTimedPayment("alan","2");
	}
}
