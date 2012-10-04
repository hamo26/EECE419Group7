package com.calculator;

import com.calculator.service.addition.AdditionService;
import com.google.inject.name.Named;

public class Calculator extends SimpleCalculator{

	private AdditionService additionService;

	public Calculator(@Named("additionService")
					  AdditionService additionService) {
		this.additionService = additionService;
	}
	
	public double addAndIncrement(double x, double y) {
		return additionService.add(x, y) + 1;
	}
	
	
}
