package com.schedushare.core.user.rest.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * Resource to get and update schedushare users.
 */
public interface UserResource {

	/**
	 * Gets a user.
	 *
	 * @return the user
	 */
	@Get
	public String getUser();
	
	/**
	 * Updates a user.
	 *
	 * @return the string
	 */
	@Put
	public String updateUser(String userRepresentation);
}
