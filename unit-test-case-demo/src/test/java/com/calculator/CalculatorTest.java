package com.calculator;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.integration.junit3.JUnit3Mockery;

import com.calculator.service.addition.AdditionService;

/**
 * Tests {@link Calculator}.
 */
public class CalculatorTest extends TestCase {
	
	private static final double VALUE_X = 10;

	private static final double VALUE_Y = 10;

	private JUnit3Mockery context = new JUnit3Mockery();
	
	private AdditionService mockAdditionService = context.mock(AdditionService.class);
	private Calculator testCalculator = new Calculator(mockAdditionService);
	
	/**
	 * Test add and increment.
	 */
	public void testAddAndIncrement() {
		context.checking(new Expectations() {
			{
				allowing(mockAdditionService).add(VALUE_X, VALUE_Y);
				will(returnValue(VALUE_X + VALUE_Y));
			}
		});
		
		double additionResult = testCalculator.addAndIncrement(VALUE_X, VALUE_Y);
		double expectedAdditionResult = VALUE_X + VALUE_Y + 1;
		assertEquals("The result should be x + y + 1", expectedAdditionResult, additionResult);
	}

}
