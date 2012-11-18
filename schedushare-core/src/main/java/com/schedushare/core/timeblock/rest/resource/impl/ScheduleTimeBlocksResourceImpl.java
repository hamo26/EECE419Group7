package com.schedushare.core.timeblock.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.timeblock.rest.resource.ScheduleTimeBlocksResource;
import com.schedushare.core.timeblocks.service.TimeBlocksService;

/**
 * Implements {@link ScheduleTimeBlocksResource}.
 */
public class ScheduleTimeBlocksResourceImpl extends SelfInjectingServerResource
		implements ScheduleTimeBlocksResource {

	private Connection connection;
	
	@Inject
	private TimeBlocksService timeBlocksService;
	
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
	@Get
	public String getTimeBlocksForSchedule() {
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			TimeBlocksEntity timeBlocksForScheduleEntity = timeBlocksService.getTimeBlocksForSchedule(connection, scheduleId);
			
			return jsonUtil.serializeRepresentation(timeBlocksForScheduleEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

	@Override
	@Put
	public String updateTimeBlocksForDay(String timeBlocksRepresentation) {
		try{
			TimeBlocksEntity timeBlocksEntity = jsonUtil.deserializeRepresentation(timeBlocksRepresentation, TimeBlocksEntity.class);
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			TimeBlocksEntity updateTimeBlocksByDayEntity = timeBlocksService.updateTimeBlocksByDay(connection, timeBlocksEntity, timeBlocksEntity.getTimeBlocks()
																							      .iterator()
																							      .next()
																							      .getDay());
			
			return jsonUtil.serializeRepresentation(updateTimeBlocksByDayEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

}
