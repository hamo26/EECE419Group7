package com.schedushare.core.rest.handler.impl;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.google.inject.Guice;
import com.schedushare.common.guice.SchedushareCommonModule;
import com.schedushare.core.guice.SelfInjectingServerResourceModule;
import com.schedushare.core.guice.ServiceModule;
import com.schedushare.core.guice.TransactionModule;
import com.schedushare.core.schedule.rest.resource.impl.ActiveScheduleResourceImpl;
import com.schedushare.core.schedule.rest.resource.impl.ScheduleResourceImpl;
import com.schedushare.core.schedule.rest.resource.impl.UserScheduleResourceImpl;
import com.schedushare.core.timeblock.rest.resource.impl.ScheduleTimeBlocksResourceImpl;
import com.schedushare.core.timeblock.rest.resource.impl.TimeBlocksResourceImpl;
import com.schedushare.core.user.rest.resource.impl.RegisterUserResourceImpl;
import com.schedushare.core.user.rest.resource.impl.UserResourceImpl;

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
     * TODO: Create a common module for injections in the schedushare commons package.
     * TODO: Add transaction module to ensure atomic transacions.
     */  
    @Override  
    public synchronized Restlet createInboundRoot() {  
        Router router = new Router(getContext());  
        
        Guice.createInjector(
        		new ServiceModule(),
        		new SelfInjectingServerResourceModule(),
        		new SchedushareCommonModule(),
        		new TransactionModule());
        
        //Schedule routes
        router.attach("/schedules", ScheduleResourceImpl.class);
        router.attach("/schedules/{scheduleId}", ScheduleResourceImpl.class);
        router.attach("/schedules/users/{userId}", UserScheduleResourceImpl.class);
        router.attach("/schedules/active/{userId}", ActiveScheduleResourceImpl.class);

        //User routes
        router.attach("/users", RegisterUserResourceImpl.class);
        router.attach("/users/{userId}", UserResourceImpl.class);
        
        //Time block routes
        router.attach("/timeblocks", TimeBlocksResourceImpl.class);
        router.attach("/timeblocks/{timeBlockId}", TimeBlocksResourceImpl.class);
        router.attach("/timeblocks/schedule/{scheduleId}", ScheduleTimeBlocksResourceImpl.class);
        return router;  
    }  
}
