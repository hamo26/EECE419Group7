package com.schedushare.core.schedule.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.ScheduleListEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.schedule.rest.resource.UserScheduleResource;
import com.schedushare.core.schedule.service.ScheduleService;

public class UserScheduleResourceImpl extends SelfInjectingServerResource
		implements UserScheduleResource {

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
	@Get
	public String getSchedulesForUser() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			ScheduleListEntity schedulesForUser = scheduleService.getSchedulesForUser(connection, userId);
			return jsonUtil.serializeRepresentation(schedulesForUser);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(
					e.getMessage()).serializeJsonException();
		}
	}

	@Override
	@Post
	public String createScheduleForUser(String scheduleRepresentation) {
		try{
			ScheduleEntity postedSchedule = jsonUtil.deserializeRepresentation(scheduleRepresentation, ScheduleEntity.class);
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			ScheduleEntity postedScheduleEntity = scheduleService.createScheduleForUser(connection, userId, postedSchedule);
			
			return jsonUtil.serializeRepresentation(postedScheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

}
