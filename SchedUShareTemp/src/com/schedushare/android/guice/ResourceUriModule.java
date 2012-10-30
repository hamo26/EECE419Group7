package com.schedushare.android.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.schedushare.android.util.ResourceUriBuilder;

/**
 * Guice module for ResourceUri bindings.
 * 
 * Rather than hard coding all of the resource URI strings in every class.
 * I chose to inject them so if the resource URI were to change, they would only have to be changed here.
 * 
 * This Module holds the Guice bindings for all core resources being used. For the implementation
 * of the services these URIs are binding too, refer to the core project.
 * 
 */
public class ResourceUriModule extends AbstractModule {

	@Override
	protected void configure() {
		//System specific bindings. 
		//TODO: should put these in properties files.
		bind(String.class).annotatedWith(Names.named("host")).toInstance("localhost");
		bind(Integer.class).annotatedWith(Names.named("hostPort")).toInstance(8189);
		
		//Resource bindings.
		bind(String.class).annotatedWith(Names.named("loginResource")).toInstance("user");
		bind(String.class).annotatedWith(Names.named("schedulesResource")).toInstance("schedules/user");

		
		//Util Bindings
		bind(ResourceUriBuilder.class).annotatedWith(Names.named("resourceUriBuilder")).to(ResourceUriBuilder.class);
		
		
	}

}
