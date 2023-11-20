package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		/**
		 *Testing if returns correct amount for all set up Money classes
		 */
		assertEquals(10000,SEK100.getAmount(),0);
		assertEquals(1000,EUR10.getAmount(),0);
		assertEquals(20000,SEK200.getAmount(),0);
		assertEquals(2000,EUR20.getAmount(),0);
		assertEquals(0,SEK0.getAmount(),0);
		assertEquals(0,EUR0.getAmount(),0);
		assertEquals(-10000,SEKn100.getAmount(),0);
	}

	@Test
	public void testGetCurrency() {
		/**
		 *Testing if returns correct currency for all set up Money classes and checking if it will correctly identify wrong currencies for two cases
		 */
		assertSame(SEK,SEK100.getCurrency());
		assertSame(EUR,EUR10.getCurrency());
		assertSame(SEK,SEK200.getCurrency());
		assertSame(EUR,EUR20.getCurrency());
		assertSame(SEK,SEK0.getCurrency());
		assertSame(EUR,EUR0.getCurrency());
		assertSame(SEK,SEKn100.getCurrency());
		assertNotSame(EUR,SEKn100.getCurrency());
		assertNotSame(SEK,EUR10.getCurrency());
	}

	@Test
	public void testToString() {
		/**
		 *Testing if returns expected string for four cases, that are quite dissimilar
		 */
		assertEquals("100 SEK",SEK100.toString());
		assertEquals("20 EUR",EUR20.toString());
		assertEquals("0 EUR",EUR0.toString());
		assertEquals("-100 SEK",SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		/**
		 *Testing if returns expected value for four cases, that are quite dissimilar
		 */
		assertEquals(1500,SEK100.universalValue(),0);
		assertEquals(1500,EUR10.universalValue(),0);
		assertEquals(0,EUR0.universalValue(),0);
		assertEquals(-1500,SEKn100.universalValue(),0);
	}

	@Test
	public void testEqualsMoney() {
		/**
		 *Testing if returns correct boolean for three dissimilar cases:
		 * one true that is not immediately obvious
		 * one true that is obvious
		 * one false
		 */
		assertTrue("true",SEK100.equals(EUR10));
		assertTrue("true",SEK0.equals(EUR0));
		assertFalse("false",EUR20.equals(SEKn100));
	}

	@Test
	public void testAdd() {
		/**
		 *Testing if returns expected value for three cases, that are quite dissimilar
		 */
		assertEquals(20000,SEK100.add(EUR10).getAmount(),0);
		assertEquals(10000,SEKn100.add(EUR20).getAmount(),0);
		assertEquals(20000,SEK200.add(EUR0).getAmount(),0);
	}

	@Test
	public void testSub() {
		/**
		 *Testing if returns expected value for three cases, that are quite dissimilar
		 */
		assertEquals(0,SEK100.sub(EUR10).getAmount(),0);
		assertEquals(-30000,SEKn100.sub(EUR20).getAmount(),0);
		assertEquals(20000,SEK200.sub(EUR0).getAmount(),0);
	}

	@Test
	public void testIsZero() {
		/**
		 *Testing if returns correct boolean, with two cases that should be true and two that should be false
		 */
		assertFalse("false",SEK100.isZero());
		assertTrue("true",SEK0.isZero());
		assertFalse("false",EUR20.isZero());
		assertTrue("true",EUR0.isZero());
	}

	@Test
	public void testNegate() {
		/**
		 *Testing if the string that will be received after negating the amount of money is correct
		 */
		assertEquals("-100 SEK",SEK100.negate().toString());
		assertEquals("-20 EUR",EUR20.negate().toString());
		assertEquals("0 SEK",SEK0.negate().toString());
	}

	@Test
	public void testCompareTo() {
		/**
		 *Testing if the method compares two money classes correctly for four cases:
		 * one that is not immediately obvious that they are equal
		 * one that is obvious that they are equal
		 * one where the value is bigger than the other
		 * one where the value is smaller than the other
		 */
		assertEquals(0,SEK100.compareTo(EUR10));
		assertEquals(0,SEK0.compareTo(EUR0));
		assertEquals(1,EUR20.compareTo(SEKn100));
		assertEquals(-1,SEK0.compareTo(SEK200));
	}
}
