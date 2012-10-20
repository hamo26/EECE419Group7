package com.schedushare.core.user.rest.resource.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.schedushare.core.user.rest.resource.AuthenticationResource;

/**
 * Tests the {@link AuthenticationResourceImpl}.
 */
public class AuthenticationResourceImplTest {

	final AuthenticationResource authenticationResource = new AuthenticationResourceImpl();

	/**
	 * Test authenticate user.
	 */
	@Test
	public void testAuthenticateUser() {
		String authenticationToken = authenticationResource.authenticateUser("");
		assertNull(authenticationToken);
	}

}
