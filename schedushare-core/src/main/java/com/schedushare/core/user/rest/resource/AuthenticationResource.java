package com.schedushare.core.user.rest.resource;

import org.restlet.resource.Post;

/**
 * Resource used to authenticate a user via the facebook api.
 */
public interface AuthenticationResource {

	/**
	 * Authenticate a user.
	 *
	 * @param userRepresentation the user representation
	 * @return an authetntication token for that user
	 */
	@Post
	public String authenticateUser(String userRepresentation);
}
