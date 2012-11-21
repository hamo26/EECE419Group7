package com.schedushare.core.user.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.user.rest.resource.UserResource;
import com.schedushare.core.user.service.UserService;

public class UserResourceImpl extends SelfInjectingServerResource implements
		UserResource {

	private String userId;
	private Connection connection;
	
	@Inject
	@Named("jsonUtil")
	private JSONUtil jsonUtil;
	
	@Inject
	private SchedushareExceptionFactory schedushareExceptionFactory;
	
	@Inject
	private UserService userService;
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		this.userId = (String) getRequestAttributes().get("userId");
	}
	
	@Override
	@Get
	public String getUser() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			UserEntity user = userService.getUser(connection, userId);
			return jsonUtil.serializeRepresentation(user);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(
					e.getMessage()).serializeJsonException();
		}
	}

	@Override
	@Put
	public String updateUser(String userRepresentation) {
		try {
			UserEntity updatedUser = jsonUtil.deserializeRepresentation(userRepresentation, UserEntity.class);
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			UserEntity updatedUserEntity = userService.updateUser(connection, updatedUser);
			return jsonUtil.serializeRepresentation(updatedUserEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(
					e.getMessage()).serializeJsonException();
		}
	}

}
