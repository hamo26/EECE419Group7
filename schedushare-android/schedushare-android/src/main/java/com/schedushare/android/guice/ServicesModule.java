package com.schedushare.android.guice;

import org.springframework.web.client.RestTemplate;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.schedushare.android.user.task.LoginTask;

/**
 * Guice module for service bindings.
 */
public class ServicesModule extends AbstractModule {

	@Override
	protected void configure() {
		//Spring rest client bindings.
		bind(RestTemplate.class).annotatedWith(Names.named("restTemplate")).to(RestTemplate.class);
		
		//service bindings
		//TODO: these bindings are unnecessary because the Guice provider can find the classes without
		//      annotations. Look into deleting these at some point.
		bind(LoginTask.class).annotatedWith(Names.named("loginTask")).to(LoginTask.class);
	}
}
