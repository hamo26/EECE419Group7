package com.schedushare.core.timeblocks.service.impl;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.core.database.SchedushareFactory;
import com.schedushare.core.database.Tables;
import com.schedushare.core.schedule.service.ScheduleService;
import com.schedushare.core.timeblocks.service.TimeBlocksService;

/**
 * Implements {@link TimeBlocksService}.
 */
public class TimeBlocksServiceImpl implements TimeBlocksService {

	private SchedushareExceptionFactory schedushareExceptionFactory;
	
	private ScheduleService scheduleService;
	
	/**
	 * Default constructor.
	 *
	 * @param schedushareExceptionFactory the schedushare exception factory
	 * @param scheduleService the schedule service
	 */
	@Inject
	public TimeBlocksServiceImpl(
			final SchedushareExceptionFactory schedushareExceptionFactory,
			final ScheduleService scheduleService) {
		
		this.schedushareExceptionFactory = schedushareExceptionFactory;
		this.scheduleService = scheduleService;
	}
	
	@Override
	public TimeBlockEntity getTimeBlock(Connection connection,
			int timeBlockId) throws SchedushareException {
		// TODO Auto-generated method stub
		SchedushareFactory getTimeBlockQuery = new SchedushareFactory(connection);
		List<TimeBlockEntity> queryResult = getTimeBlockQuery.select()
						 .from(Tables.TIMEBLOCK)
						 .where(Tables.TIMEBLOCK.ID.equal(timeBlockId))
						 .fetchInto(TimeBlockEntity.class);
		if (queryResult.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("Time block does not exist.");
		} else {
			return queryResult.get(0);
		}
	}

	@Override
	public TimeBlocksEntity getTimeBlocksForSchedule(
			Connection connection, int scheduleId) throws SchedushareException {
		SchedushareFactory getTimeBlocksQuery = new SchedushareFactory(connection);
		
		try {
			scheduleService.getSchedule(connection, scheduleId);
			
			List<TimeBlockEntity> timeBlockEntitiesResult = getTimeBlocksQuery.select()
					.from(Tables.TIMEBLOCK)
					.where(Tables.TIMEBLOCK.SCHEDULE_ID.equal(scheduleId))
					.fetchInto(TimeBlockEntity.class);
			return new TimeBlocksEntity(timeBlockEntitiesResult);
		} catch (SchedushareException e) {
			throw e;
		} catch (Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
	}

	@Override
	public TimeBlocksEntity createTimeBlocks(Connection connection,
			int scheduleId, TimeBlocksEntity timeBlocksEntity) throws SchedushareException {
		SchedushareFactory createTimeBlocksQuery = new SchedushareFactory(connection);
		
		try {
			scheduleService.getSchedule(connection, scheduleId);
			Collection<TimeBlockEntity> timeBlocks = timeBlocksEntity.getTimeBlocks();
			for (TimeBlockEntity timeBlock : timeBlocks) {
				createTimeBlocksQuery.insertInto(Tables.TIMEBLOCK, Tables.TIMEBLOCK.DAY, Tables.TIMEBLOCK.END_TIME, Tables.TIMEBLOCK.LATITUDE,
						Tables.TIMEBLOCK.LONGITUDE, Tables.TIMEBLOCK.SCHEDULE_ID, Tables.TIMEBLOCK.START_TIME)
						.values(timeBlock.getDay(), timeBlock.getEndTime(), timeBlock.getLatitude(), timeBlock.getLongitude(),
								scheduleId, timeBlock.getStartTime());
			}
			
			List<TimeBlockEntity> timeBlockEntitiesResult = createTimeBlocksQuery.select()
					.from(Tables.TIMEBLOCK)
					.where(Tables.TIMEBLOCK.SCHEDULE_ID.equal(scheduleId))
					.fetchInto(TimeBlockEntity.class);
			return new TimeBlocksEntity(timeBlockEntitiesResult);
		} catch (SchedushareException e) {
			throw e;
		} catch(Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
	}

	@Override
	public TimeBlockEntity updateTimeBlock(Connection connection,
			int timeBlockId) throws SchedushareException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeBlockEntity deleteTimeBlock(Connection connection,
			int timeBlockId) throws SchedushareException {
		// TODO Auto-generated method stub
		return null;
	}

}
