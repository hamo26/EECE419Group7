package com.schedushare.core.user.rest.resource.impl;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Router;

import com.google.inject.Guice;
import com.schedushare.common.guice.SchedushareCommonModule;
import com.schedushare.core.guice.SelfInjectingServerResourceModule;
import com.schedushare.core.guice.ServiceModule;
import com.schedushare.core.user.rest.resource.RegisterUserResource;

public class RegisterUserResourceImplTest extends Application {

	@Test 
	public void testReturnsErrorWhenEmptyUserPosted() {
        ClientResource client = new ClientResource("http://localhost:8118");
        String msg = client.getChild("/user", RegisterUserResource.class).registerUser("{}");
        assertEquals(msg, "{\"schedushare-error\":\"SQL [insert into `USER` (`ID`, `EMAIL`, `NAME`, `AUTH_TOKEN`) values (?, ?, ?, ?)]; " +
        					"Column \\u0027ID\\u0027 cannot be null\"}");
    }

    /**
	 * Creates the injector.
	 */
	@Before 
	public void createInjector() {
        Guice.createInjector(
            new ServiceModule(),
            new SelfInjectingServerResourceModule(),
            new SchedushareCommonModule()
        );
    }

    /**
	 * Starts internal Rest server.
	 *
	 * @throws Exception the exception
	 */
	@Before 
	public void startComponent() throws Exception {
        component = new Component();
        component.getServers().add(Protocol.HTTP, 8118);
        component.getDefaultHost().attachDefault(this);
        component.start();
    }

    /**
	 * Stops rest server.
	 *
	 * @throws Exception the exception
	 */
	@After public void stopComponent() throws Exception {
        component.stop();
    }

    private volatile Component component;


    @Override public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/user", RegisterUserResourceImpl.class);
        return router;
    }
}
