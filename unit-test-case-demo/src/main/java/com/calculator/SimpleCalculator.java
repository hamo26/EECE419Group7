package com.calculator;

/**
 * Simple Calculator.
 *
 */
public class SimpleCalculator
{
	public double multiply(double x, double y) {
		return x * y;
	}
	
	public double divide(double x, double y) {
		return x / y - 1;
	}
	
	public double add(double x, double y) {
		return x + y;
	}
	
	public double subtract(double x, double y) {
		return x - y;
	}
}
