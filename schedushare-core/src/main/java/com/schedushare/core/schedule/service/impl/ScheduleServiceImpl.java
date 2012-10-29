package com.schedushare.core.schedule.service.impl;

import java.sql.Connection;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.jooq.Field;
import org.jooq.impl.Factory;

import com.google.inject.Inject;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.core.database.SchedushareFactory;
import com.schedushare.core.database.Tables;
import com.schedushare.core.schedule.service.ScheduleService;
import com.schedushare.core.user.service.UserService;

public class ScheduleServiceImpl implements ScheduleService {

	private final SchedushareExceptionFactory schedushareExceptionFactory;
	private final UserService userService;
	
	@Inject
	public ScheduleServiceImpl(final SchedushareExceptionFactory schedushareExceptionFactory,
							   final UserService userService) {
		this.schedushareExceptionFactory = schedushareExceptionFactory;
		this.userService = userService;
		
	}
	
	@Override
	public ScheduleEntity getSchedule(Connection connection, int scheduleId)
			throws SchedushareException {
		
		SchedushareFactory getScheduleQuery = new SchedushareFactory(connection);
		
		List<ScheduleEntity> queryResult = getScheduleQuery.select()
						.from(Tables.SCHEDULE)
						.where(Tables.SCHEDULE.ID.equal(scheduleId))
						.fetchInto(ScheduleEntity.class);
		
		ScheduleEntity retrievedSchedule = queryResult.get(0);
		if (queryResult.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("Schedule with id: " + scheduleId + " does not exist");
		} else {
			SchedushareFactory getTimeblocksQuery = new SchedushareFactory(connection);
			List<TimeBlockEntity> getTimeblocksQueryResult = getTimeblocksQuery.select()
								.from(Tables.TIMEBLOCK)
								.where(Tables.TIMEBLOCK.SCHEDULE_ID.equal(scheduleId))
								.fetchInto(TimeBlockEntity.class);
			 
			ScheduleEntity getScheduleResult = new ScheduleEntity(retrievedSchedule.getScheduleId(), retrievedSchedule.getScheduleName(), 
					retrievedSchedule.isScheduleActive(), retrievedSchedule.getUserId(), retrievedSchedule.getT_lastModified().toString(),
					getTimeblocksQueryResult);
			return getScheduleResult;
		}
	}

	@Override
	public ScheduleEntity getActiveScheduleForUser(Connection connection,
			String userEmail) throws SchedushareException {
		
		try {
			SchedushareFactory getScheduleQuery = new SchedushareFactory(connection);
			
			UserEntity user = userService.getUser(connection, userEmail);
			ScheduleEntity scheduleEntity = getScheduleQuery.select().from(Tables.SCHEDULE)
					.where(Tables.SCHEDULE.ACTIVE.equal(Boolean.TRUE))
					.and(Tables.SCHEDULE.USER_ID.equal(user.getUserId()))
					.fetchInto(ScheduleEntity.class)
					.get(0);
			
			List<TimeBlockEntity> timeBlocks = getScheduleQuery.select()
					.from(Tables.TIMEBLOCK)
					.where(Tables.TIMEBLOCK.SCHEDULE_ID.equal(scheduleEntity.getScheduleId()))
					.fetchInto(TimeBlockEntity.class);
			
			return new ScheduleEntity(scheduleEntity.getScheduleId(), 
					scheduleEntity.getScheduleName(), 
					scheduleEntity.isScheduleActive(), 
					scheduleEntity.getUserId(), 
					scheduleEntity.getT_lastModified().toString(), 
					timeBlocks);
		} catch(SchedushareException e) {
			throw e;
		} catch (Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
	}

	@Override
	public Collection<ScheduleEntity> getSchedulesForUser(
			Connection connection, String userEmail) {
		return null;
	}


	@Override
	public ScheduleEntity createScheduleForUser(Connection connection,
			String userEmail, ScheduleEntity scheduleEntity) throws SchedushareException {

		SchedushareFactory createScheduleQuery = new SchedushareFactory(connection);
		Collection<TimeBlockEntity> timeBlocks = scheduleEntity.getTimeBlocks();
		
		try {
			//FIX THIS. DO A JOIN INSTEAD OF CALLING INTO USER SERVICE.
			UserEntity userEntity = userService.getUser(connection, userEmail);
			
			createScheduleQuery.insertInto(Tables.SCHEDULE, Tables.SCHEDULE.NAME, 
							Tables.SCHEDULE.LAST_MODIFIED, Tables.SCHEDULE.ACTIVE, Tables.SCHEDULE.USER_ID)
						   .values(scheduleEntity.getScheduleName(), new Time(Calendar.getInstance().getTimeInMillis()).getTime(), 
								  scheduleEntity.isScheduleActive().booleanValue(), userEntity.getUserId()).execute();
			//Look for a better way to do this.
			Field<?> identity = Factory.field("@@IDENTITY");
			Integer createdScheduleId = createScheduleQuery.select(identity)
																	  .from(Tables.SCHEDULE)
																	  .fetchInto(Integer.class)
																	  .get(0);
			
			for (TimeBlockEntity timeBlock : timeBlocks) {
				createScheduleQuery.insertInto(Tables.TIMEBLOCK, Tables.TIMEBLOCK.DAY, Tables.TIMEBLOCK.START_TIME,
						Tables.TIMEBLOCK.END_TIME, Tables.TIMEBLOCK.LATITUDE, Tables.TIMEBLOCK.LONGITUDE, Tables.TIMEBLOCK.SCHEDULE_ID)
						.values(timeBlock.getDay(), Time.valueOf(timeBlock.getStartTime()), Time.valueOf(timeBlock.getEndTime()), timeBlock.getLatitude(),
								timeBlock.getLongitude(), createdScheduleId).execute();
			}
		} catch (SchedushareException e) {
			throw e;
		} catch (Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
		
		return scheduleEntity;
	}

	
}
