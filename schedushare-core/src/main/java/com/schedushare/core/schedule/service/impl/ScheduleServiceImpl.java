package com.schedushare.core.schedule.service.impl;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.core.database.SchedushareFactory;
import com.schedushare.core.database.Tables;
import com.schedushare.core.schedule.service.ScheduleService;

public class ScheduleServiceImpl implements ScheduleService {

	private final SchedushareExceptionFactory schedushareExceptionFactory;
	
	public ScheduleServiceImpl(final SchedushareExceptionFactory schedushareExceptionFactory) {
		this.schedushareExceptionFactory = schedushareExceptionFactory;
		
	}
	
	@Override
	public ScheduleEntity getSchedule(Connection connection, int scheduleId)
			throws SchedushareException {
		SchedushareFactory getScheduleQuery = new SchedushareFactory(connection);
		
		List<ScheduleEntity> queryResult = getScheduleQuery.select()
						.from(Tables.SCHEDULE)
						.where(Tables.SCHEDULE.ID.equal(scheduleId))
						.fetchInto(ScheduleEntity.class);
		
		if (queryResult.isEmpty()) {
			throw schedushareExceptionFactory.createSchedushareException("Schedule with id: " + scheduleId + " does not exist");
		}
		
		return queryResult.get(0);
	}

	@Override
	public ScheduleEntity getActiveScheduleForUser(Connection connection,
			String userId) {
		return null;
	}

	@Override
	public Collection<ScheduleEntity> getSchedulesForUser(
			Connection connection, String userId) {
		return null;
	}


	@Override
	public ScheduleEntity createScheduleForUser(Connection connection,
			String userId, ScheduleEntity scheduleEntity) throws SchedushareException {

		SchedushareFactory createScheduleQuery = new SchedushareFactory(connection);
		
		try {
			createScheduleQuery.insertInto(Tables.SCHEDULE, Tables.SCHEDULE.ID, Tables.SCHEDULE.NAME, 
							Tables.SCHEDULE.LAST_MODIFIED, Tables.SCHEDULE.ACTIVE, Tables.SCHEDULE.OWNER_ID)
						   .values("", scheduleEntity.getScheduleName(), "", scheduleEntity.isScheduleActive(), userId).execute();
		} catch (Exception e) {
			throw schedushareExceptionFactory.createSchedushareException(e.getMessage());
		}
		
		return scheduleEntity;
	}

	
}
