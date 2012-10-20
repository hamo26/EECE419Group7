package com.calculator;

import junit.framework.TestCase;


/**
 * Tests {@link SimpleCalculator}.
 */
public class SimpleCalculatorTest extends TestCase {
	
	private static final double VALUE_X = 10;
	private static final double VALUE_Y = 10;
	private SimpleCalculator calculator = new SimpleCalculator();
	
	/**
	 * Test multiply.
	 */
	public void testMultiply() {
		double multiplicationResult = calculator.multiply(VALUE_X, VALUE_Y);
		assertEquals("Multiplication should return right value.", 
				VALUE_X * VALUE_Y, multiplicationResult);
	}

	/**
	 * Test divide.
	 */
	public void testDivide() {
		double divisionResult = calculator.divide(VALUE_X, VALUE_Y);
		assertEquals("Division should return right value.",
				VALUE_X / VALUE_Y, divisionResult);
	}

	/**
	 * Test add.
	 */
	public void testAdd() {
		double additionResult = calculator.add(VALUE_X, VALUE_Y);
		assertEquals("Addition should return right value.",
				VALUE_X + VALUE_Y, additionResult);
	}

	/**
	 * Test subtract.
	 */
	public void testSubtract() {
		double subtractionResult = calculator.subtract(VALUE_X, VALUE_Y);
		assertEquals("Did not get right subtraction result", VALUE_X - VALUE_Y, subtractionResult);
	}

}
