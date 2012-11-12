package com.schedushare.core.user.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.database.transaction.Transaction;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.user.rest.resource.RegisterUserResource;
import com.schedushare.core.user.service.UserService;

public class RegisterUserResourceImpl extends SelfInjectingServerResource
		implements RegisterUserResource {

	private Connection connection;
	
	@Inject
	private UserService userService;
	
	@Inject
	@Named("jsonUtil")
	private JSONUtil jsonUtil;
	
	@Inject
	private SchedushareExceptionFactory schedushareExceptionFactory;
	
	protected void doInit() throws ResourceException {
		super.doInit();
	}

	@Override
	@Post
	@Transaction
	public String registerUser(String userRepresentation) {
		try {
			UserEntity postedUser = jsonUtil.deserializeRepresentation(
					userRepresentation, UserEntity.class);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);

			UserEntity createUserEntity = userService.createUser(connection, postedUser);
			
			return jsonUtil.serializeRepresentation(createUserEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory
					.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}
}
