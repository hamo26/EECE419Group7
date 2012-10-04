package com.calculator.service.addition.impl;

import com.calculator.service.addition.AdditionService;

import junit.framework.TestCase;

/**
 * Tests {@link AdditionServiceImpl}.
 */
public class AdditionServiceImplTest extends TestCase {
	
	private static final double VALUE_X = 10;
	private static final double VALUE_Y = 10;
	
	private AdditionService testAdditionService = new AdditionServiceImpl();
	
	/**
	 * Test addition.
	 */
	public void testAddition() {
		double additionResult = testAdditionService.add(VALUE_X, VALUE_Y);
		assertEquals("Addition should return right value.",
				VALUE_X + VALUE_Y, additionResult);
	}
}
