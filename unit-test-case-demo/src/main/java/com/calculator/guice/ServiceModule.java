package com.calculator.guice;

import com.calculator.service.addition.AdditionService;
import com.calculator.service.addition.impl.AdditionServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Guice service module for dependency injection bindings.
 */
public class ServiceModule extends AbstractModule {


	@Override
	protected void configure() {
		bind(AdditionService.class).annotatedWith(Names.named("additionService")).to(AdditionServiceImpl.class);
	}

}
