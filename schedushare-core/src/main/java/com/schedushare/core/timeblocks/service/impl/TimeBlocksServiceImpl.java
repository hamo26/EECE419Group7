package com.schedushare.core.timeblocks.service.impl;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.jooq.InsertValuesStep;

import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.core.database.SchedushareFactory;
import com.schedushare.core.database.Tables;
import com.schedushare.core.database.enums.TimeblockDay;
import com.schedushare.core.database.tables.records.TimeblockRecord;
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
			return new TimeBlocksEntity(scheduleId, timeBlockEntitiesResult);
		} catch (SchedushareException e) {
			throw e;
		} catch (Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
	}

	@Override
	public TimeBlocksEntity createTimeBlocks(Connection connection, TimeBlocksEntity timeBlocksEntity) throws SchedushareException {
		SchedushareFactory createTimeBlocksQuery = new SchedushareFactory(connection);
		
		try {
			int scheduleId = timeBlocksEntity.getScheduleId();
			scheduleService.getSchedule(connection, scheduleId);
			Collection<TimeBlockEntity> timeBlocks = timeBlocksEntity.getTimeBlocks();
			InsertValuesStep<TimeblockRecord> insertIntoTimeBlock = createTimeBlocksQuery.insertInto(Tables.TIMEBLOCK, 
															   										 Tables.TIMEBLOCK.DAY, 
															   										 Tables.TIMEBLOCK.END_TIME, 
															   										 Tables.TIMEBLOCK.LATITUDE,
															   										 Tables.TIMEBLOCK.LONGITUDE, 
															   										 Tables.TIMEBLOCK.SCHEDULE_ID, 
															   										 Tables.TIMEBLOCK.START_TIME);
			for (TimeBlockEntity timeBlock : timeBlocks) {
						insertIntoTimeBlock.values(timeBlock.getDay(), 
												   timeBlock.getEndTime(), 
												   timeBlock.getLatitude(), 
												   timeBlock.getLongitude(),
												   scheduleId, 
												   timeBlock.getStartTime());
			}
			insertIntoTimeBlock.execute();
			
			List<TimeBlockEntity> timeBlockEntitiesResult = createTimeBlocksQuery.select()
					.from(Tables.TIMEBLOCK)
					.where(Tables.TIMEBLOCK.SCHEDULE_ID.equal(scheduleId))
					.fetchInto(TimeBlockEntity.class);
			return new TimeBlocksEntity(scheduleId, timeBlockEntitiesResult);
		} catch (SchedushareException e) {
			throw e;
		} catch(Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
	}

	@Override
	public TimeBlocksEntity updateTimeBlocks(Connection connection, TimeBlocksEntity timeBlocksEntity) throws SchedushareException {
		SchedushareFactory updateTimeBlockQuery = new SchedushareFactory(connection);

		try {
			Collection<TimeBlockEntity> timeBlocks = timeBlocksEntity.getTimeBlocks();
			
			for (TimeBlockEntity timeBlock : timeBlocks) {
				updateTimeBlockQuery.update(Tables.TIMEBLOCK)
									.set(Tables.TIMEBLOCK.DAY, TimeblockDay.valueOf(timeBlock.getDay()))
									.set(Tables.TIMEBLOCK.END_TIME, timeBlock.getT_endTime())
									.set(Tables.TIMEBLOCK.LATITUDE, timeBlock.getLatitude())
									.set(Tables.TIMEBLOCK.LONGITUDE, timeBlock.getLongitude())
									.set(Tables.TIMEBLOCK.START_TIME, timeBlock.getT_startTime())
									.where(Tables.TIMEBLOCK.ID.equal(timeBlock.getTimeBlockId()))
									.execute();
			}
		} catch (Exception e) {
			schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
		return getTimeBlocksForSchedule(connection, timeBlocksEntity.getScheduleId());
	}

	@Override
	public TimeBlockEntity deleteTimeBlock(Connection connection,
			int timeBlockId) throws SchedushareException {
		SchedushareFactory deleteTimeBlockQuery = new SchedushareFactory(connection);

		try {	
			TimeBlockEntity timeBlockEntity = getTimeBlock(connection, timeBlockId);
			deleteTimeBlockQuery.delete(Tables.TIMEBLOCK)
								.where(Tables.TIMEBLOCK.ID.equal(timeBlockId))
								.execute();
			return timeBlockEntity;
		} catch (SchedushareException e) {
			throw e;
		} catch (Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
	}

}
