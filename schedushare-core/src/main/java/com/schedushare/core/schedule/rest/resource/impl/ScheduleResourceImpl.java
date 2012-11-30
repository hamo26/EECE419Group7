package com.schedushare.core.schedule.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Delete;
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
import com.schedushare.core.schedule.rest.resource.ScheduleResource;
import com.schedushare.core.schedule.service.UserScheduleService;


/**
 * Implements {@link ScheduleResource}.
 */
public class ScheduleResourceImpl extends SelfInjectingServerResource implements ScheduleResource {

	private Connection connection;
	
	@Inject
	private UserScheduleService scheduleService;
	
	@Inject
	@Named("jsonUtil")
	private JSONUtil jsonUtil;
	
	@Inject
	private SchedushareExceptionFactory schedushareExceptionFactory;

	private int scheduleId;
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		Object id = getRequestAttributes().get("scheduleId");
		this.scheduleId = (id == null) ? 0 : (Integer.valueOf((String)id));
    }


	@Override
	@Put
	public String updateSchedule(String scheduleRepresentation) {
		try{
			ScheduleEntity scheduleEntity = jsonUtil.deserializeRepresentation(scheduleRepresentation, ScheduleEntity.class);
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			ScheduleEntity updatedScheduleEntity = scheduleService.updateSchedule(connection, scheduleEntity);
			
			return jsonUtil.serializeRepresentation(updatedScheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

	@Override
	@Delete
	public String deleteSchedule() {
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			ScheduleEntity deletedScheduleEntity = scheduleService.deleteSchedule(connection, scheduleId);
			
			return jsonUtil.serializeRepresentation(deletedScheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}


	@Override
	@Get
	public String getSchedule() {
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			ScheduleEntity scheduleEntity = scheduleService.getSchedule(connection, scheduleId);
			
			return jsonUtil.serializeRepresentation(scheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
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
			
			ScheduleEntity postedScheduleEntity = scheduleService.createScheduleForUser(connection, postedSchedule);
			
			return jsonUtil.serializeRepresentation(postedScheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}
}
