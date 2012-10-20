package com.schedushare.core.user.rest.resource.impl;

import org.restlet.resource.ResourceException;

import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.user.rest.resource.AuthenticationResource;


/**
 * Implementation of {@link AuthenticationResource}.
 * 
 */
public class AuthenticationResourceImpl extends SelfInjectingServerResource implements AuthenticationResource {
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
    }

	@Override
	public String authenticateUser(String userRepresentation) {
		return null;
	}
}

