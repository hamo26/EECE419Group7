package com.schedushare.core.user.rest.resource;

import org.restlet.resource.Post;

import com.google.inject.ImplementedBy;
import com.schedushare.core.database.transaction.Transaction;
import com.schedushare.core.user.rest.resource.impl.RegisterUserResourceImpl;

/**
 * Resource used to register a schedushare user.
 */
@ImplementedBy(RegisterUserResourceImpl.class)
public interface RegisterUserResource {
	
	/**
	 * Register a user.
	 *
	 * @param userRepresentation the user representation
	 * @return the string
	 */
	@Post
	@Transaction
	public String registerUser(String userRepresentation);
	

}
