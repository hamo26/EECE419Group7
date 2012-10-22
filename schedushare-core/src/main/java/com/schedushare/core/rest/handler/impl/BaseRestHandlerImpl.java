package com.schedushare.core.rest.handler.impl;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.google.inject.Guice;
import com.schedushare.core.guice.SelfInjectingServerResourceModule;
import com.schedushare.core.guice.ServiceModule;

/**
 * The Base Rest Handler which defines all url binding and delegates to the appropriate resource.
 * 
 * TO DO: look at a cleaner way of binding. This method isn't bad but could do better.
 * An approach that comes to mind is spring @Path bindings.
 */
public class BaseRestHandlerImpl extends Application {
	
	public BaseRestHandlerImpl() {
		//Empty Constructor.
	}
	
	/** 
     * Creates a root Restlet that will receive all incoming calls.
     * It is here that we define all routes from uri to resource. 
     * 
     * TODO: Create a commone module for injections in the schedushare commons package.
     * TODO: Add transaction module to ensure atomic transacions.
     */  
    @Override  
    public synchronized Restlet createInboundRoot() {  
        Router router = new Router(getContext());  
        
        Guice.createInjector(
        		new ServiceModule(),
        		new SelfInjectingServerResourceModule());
        
      // router.attach("/user/login", AuthenticationResourceImpl.class);  
        return router;  
    }  
}