package com.schedushare.core.schedule.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.schedule.rest.resource.ActiveScheduleResource;
import com.schedushare.core.schedule.service.ScheduleService;

public class ActiveScheduleResourceImpl extends SelfInjectingServerResource
		implements ActiveScheduleResource {
	
	private Connection connection;
	
	@Inject
	private ScheduleService scheduleService;
	
	@Inject
	@Named("jsonUtil")
	private JSONUtil jsonUtil;
	
	@Inject
	private SchedushareExceptionFactory schedushareExceptionFactory;

	private int userId;
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		Object id = getRequestAttributes().get("userId");
		this.userId = (id == null) ? 0 : (Integer.valueOf((String)id));
    }
	@Override
	public String getActiveScheduleForUser() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			ScheduleEntity activeScheduleForUserEntity = scheduleService.getActiveScheduleForUser(connection, userId);
			return jsonUtil.serializeRepresentation(activeScheduleForUserEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(
					e.getMessage()).serializeJsonException();
		}
	}

}
