package com.schedushare.core.schedule.service;

import java.sql.Connection;
import java.util.Collection;

import com.google.inject.ImplementedBy;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.core.schedule.service.impl.ScheduleServiceImpl;

/**
 * The schedule service talkign directly to the database.
 */
@ImplementedBy(ScheduleServiceImpl.class)
public interface ScheduleService {

	/**
	 * Gets a schedule.
	 *
	 * @param connection the connection
	 * @param scheduleId the schedule id
	 * @return the schedule
	 * @throws SchedushareException the schedushare exception
	 */
	ScheduleEntity getSchedule(final Connection connection, final int scheduleId) throws SchedushareException;
	
	/**
	 * Gets the active schedule for a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @return the active schedule for user
	 */
	ScheduleEntity getActiveScheduleForUser(final Connection connection, final String userId);
	
	/**
	 * Creates a schedule for a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @param scheduleEntity the schedule entity
	 * @return the schedule entity
	 */
	ScheduleEntity createScheduleForUser(final Connection connection, final String userId, final ScheduleEntity scheduleEntity) throws SchedushareException;
	
	/**
	 * Gets all schedules for a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @return the schedules for user
	 */
	Collection<ScheduleEntity> getSchedulesForUser(final Connection connection, final String userId);
}
