package com.schedushare.core.guice;

import java.util.concurrent.atomic.AtomicBoolean;

import org.restlet.resource.ServerResource;

import com.google.inject.Inject;

/**
 * Resources that extend this class have injection capabilities and can inject 
 * some of the utilities and services we need.
 */
public abstract class SelfInjectingServerResource extends ServerResource {
	/**
	 * Implemented by DI framework-specific code. For example, with Guice, the
	 * statically-injected MembersInjector just calls
	 * {@code injector.injectMembers(object)}.
	 */
	public interface MembersInjector {
		void injectMembers(Object object);
	}

	/**
	 * Subclasseses overriding this method must call {@code super.doInit()}
	 * first.
	 */
	protected void doInit() {
		if (injected.compareAndSet(false, true)) {
			memberInjector.injectMembers(this);
		}
	}

	@Inject
	private void injected() {
		injected.set(true);
	}

	/**
	 * Whether we've been injected yet. This protects against multiple injection
	 * of a subclass that gets injected before doInit is called.
	 */
	private final AtomicBoolean injected = new AtomicBoolean(false);

	/**
	 * Must be statically injected by DI framework.
	 */
	@Inject
	private static volatile MembersInjector memberInjector;
}
