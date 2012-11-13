package com.schedushare.core.timeblock.rest.resource.impl;

import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.util.JSONUtil;
import com.schedushare.core.database.SchedusharePersistenceConstants;
import com.schedushare.core.guice.SelfInjectingServerResource;
import com.schedushare.core.timeblock.rest.resource.TimeBlocksResource;
import com.schedushare.core.timeblocks.service.TimeBlocksService;

/**
 * Implementation of {@link TimeBlocksResource}.
 */
public class TimeBlocksResourceImpl extends SelfInjectingServerResource
		implements TimeBlocksResource {

	private Connection connection;
	
	@Inject
	@Named("jsonUtil")
	private JSONUtil jsonUtil;
	
	@Inject
	private TimeBlocksService timeBlocksService;
	
	@Inject
	private SchedushareExceptionFactory schedushareExceptionFactory;
	
	private int timeBlockId;
	
	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		Object id = getRequestAttributes().get("timeBlockId");
		this.timeBlockId = (id == null) ? 0 : (Integer.valueOf((String)id)); 
	}

	@Override
	@Post
	public String createTimeBlocks(String timeBlocksRepresentation) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			TimeBlocksEntity timeBlocksEntity = jsonUtil.deserializeRepresentation(timeBlocksRepresentation, TimeBlocksEntity.class);
			TimeBlocksEntity createdTimeBlocks = timeBlocksService.createTimeBlocks(connection, timeBlocksEntity);
			return jsonUtil.serializeRepresentation(createdTimeBlocks);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

	
	@Override
	@Put
	public String updateTimeBlocks(String timeBlocksRepresentation) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			TimeBlocksEntity timeBlocksEntity = jsonUtil.deserializeRepresentation(timeBlocksRepresentation, TimeBlocksEntity.class);
			TimeBlocksEntity updatedTimeBlocks = timeBlocksService.updateTimeBlocks(connection, timeBlocksEntity);
			return jsonUtil.serializeRepresentation(updatedTimeBlocks);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

	@Override
	@Delete
	public String deleteTimeBlocks() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			
			TimeBlockEntity deletedTimeBlockEntity = timeBlocksService.deleteTimeBlock(connection, timeBlockId);
			return jsonUtil.serializeRepresentation(deletedTimeBlockEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}

	@Override
	@Get
	public String getTimeBlock() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					SchedusharePersistenceConstants.SCHEDUSHARE_URL,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT,
					SchedusharePersistenceConstants.SCHEDUSHARE_ROOT_PASSWORD);
			TimeBlockEntity timeBlockEntity = timeBlocksService.getTimeBlock(connection, timeBlockId);
			return jsonUtil.serializeRepresentation(timeBlockEntity);
		} catch (SchedushareException e) {
			return e.serializeJsonException();
		} catch (Exception e) {
			return schedushareExceptionFactory.createSchedushareException(e.getMessage())
					.serializeJsonException();
		}
	}
}
