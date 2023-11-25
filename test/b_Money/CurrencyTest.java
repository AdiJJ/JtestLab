package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	/**
	 * Test if getter function returns the correct name of the currency
	 */
	@Test
	public void testGetName() {
		/**
		 *Testing if returns correct name for all set up currencies
		 */
		assertEquals("SEK",SEK.getName());
		assertEquals("DKK",DKK.getName());
		assertEquals("EUR",EUR.getName());
	}

	/**
	 * Test if getter function returns the correct rate of the currency
	 */
	@Test
	public void testGetRate() {
		/**
		 *Testing if returns correct rate for all set up currencies
		 */
		assertEquals(0.15,SEK.getRate(),0);
		assertEquals(0.20,DKK.getRate(),0);
		assertEquals(1.5,EUR.getRate(),0);
	}
	/**
	 * Test if setter function sets the rate for the currency correctly
	 */
	@Test
	public void testSetRate() {
		/**
		 *setting new currency rate and testing if returns correct rate after
		 */
		SEK.setRate(0.16);
		assertEquals(0.16,SEK.getRate(),0);
		DKK.setRate(0.21);
		assertEquals(0.21,DKK.getRate(),0);
		EUR.setRate(1.6);
		assertEquals(1.6,EUR.getRate(),0);
	}
	/**
	 * Test if the method converts the amount of cash of this currency to a global value correctly
	 */
	@Test
	public void testGlobalValue() {
		/**
		 *Testing all currencies for one amount
		 */
		assertEquals(51,SEK.universalValue(345),0);
		assertEquals(69,DKK.universalValue(345),0);
		assertEquals(517,EUR.universalValue(345),0);
	}

	/**
	 * Test if the method converts the amount of cash to this currency correctly
	 */
	@Test
	public void testValueInThisCurrency() {
		/**
		 *Testing four different conversions with one amount of money
		 */
		assertEquals(460,SEK.valueInThisCurrency(345,DKK),0);
		assertEquals(255,DKK.valueInThisCurrency(345,SEK),0);
		assertEquals(34,EUR.valueInThisCurrency(345,SEK),0);
		assertEquals(3446,SEK.valueInThisCurrency(345,EUR),0);
	}

}
