package com.schedushare.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.schedushare.core.guice.SelfInjectingServerResource.MembersInjector;

/**
 * Guice module for self injecting resources.
 * 
 * Borrowed from http://tembrel.blogspot.ca/2012/03/restlet-guice-extension-considered.html.
 * 
 * Great article written by Timothy Peieris.
 */
public class SelfInjectingServerResourceModule extends AbstractModule {

	@Override
	protected final void configure() {
		requestStaticInjection(SelfInjectingServerResource.class);
	}

	@Provides
	MembersInjector memberInjector(final Injector injector) {
		return new MembersInjector() {
			public void injectMembers(Object object) {
				injector.injectMembers(object);
			}
		};
	}

}
