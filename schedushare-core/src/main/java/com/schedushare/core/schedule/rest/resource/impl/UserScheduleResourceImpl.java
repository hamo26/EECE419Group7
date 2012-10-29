package com.schedushare.core.schedule.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.schedule.rest.resource.UserScheduleResource;
import com.schedushare.core.schedule.service.ScheduleService;


/**
 * Implements {@link UserScheduleResource}.
 */
public class UserScheduleResourceImpl extends SelfInjectingServerResource implements UserScheduleResource {

	private static final String ACTIVE = "active";

	private Connection connection;
	
	@Inject
	private ScheduleService scheduleService;
	
	@Inject
	@Named("jsonUtil")
	private JSONUtil jsonUtil;
	
	@Inject
	private SchedushareExceptionFactory schedushareExceptionFactory;

	private String userEmail;

	private String active;
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		this.userEmail = (String) getRequestAttributes().get("userEmail");
		this.active = (String) getRequestAttributes().get(ACTIVE);
    }

	@Override
	@Post
	public String createSchedule(String scheduleRepresentation) {
		try{
			ScheduleEntity postedSchedule = jsonUtil.deserializeRepresentation(scheduleRepresentation, ScheduleEntity.class);
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			ScheduleEntity postedScheduleEntity = scheduleService.createScheduleForUser(connection, userEmail, postedSchedule);
			
			return jsonUtil.serializeRepresentation(postedScheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

	@Override
	@Put
	public String updateSchedule(String scheduleRepresentation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Get
	public String getSchedule() {
		if (active.equals(ACTIVE)) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection(
						SchedusharePersistenceConstants.SCHEDUSHARE_URL,
						SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
						SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
				ScheduleEntity scheduleEntity = scheduleService.getActiveScheduleForUser(connection, userEmail);
				return jsonUtil.serializeRepresentation(scheduleEntity);
			} catch (SchedushareException e) {
				return e.serializeJsonException();
			} catch (Exception e) {
				return schedushareExceptionFactory.createSchedushareException(
						e.getMessage()).serializeJsonException();
			}	
		} else {
			return null;
		}
	}

}
