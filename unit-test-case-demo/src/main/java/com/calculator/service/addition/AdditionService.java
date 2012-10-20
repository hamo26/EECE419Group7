package com.calculator.service.addition;

import com.calculator.service.addition.impl.AdditionServiceImpl;
import com.google.inject.ImplementedBy;

/**
 * Service to add two numbers.
 */
@ImplementedBy(AdditionServiceImpl.class)
public interface AdditionService {
	
	/**
	 * Adds values passed.
	 *
	 * @param x the first value to be added.
	 * @param y the second value to be added.
	 * @return the sum of the two values.
	 */
	public double add(double x, double y);

}
