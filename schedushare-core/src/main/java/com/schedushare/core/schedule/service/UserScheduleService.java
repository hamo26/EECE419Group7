package com.schedushare.core.schedule.service;

import java.sql.Connection;

import com.google.inject.ImplementedBy;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.ScheduleListEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.core.schedule.service.impl.UserScheduleServiceImpl;

/**
 * The schedule service talkign directly to the database.
 */
@ImplementedBy(UserScheduleServiceImpl.class)
public interface UserScheduleService {

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
	ScheduleEntity getActiveScheduleForUser(final Connection connection, final String userId) throws SchedushareException;
	
	/**
	 * Creates a schedule for a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @param scheduleEntity the schedule entity
	 * @return the schedule entity
	 * @throws SchedushareException the schedushare exception
	 */
	ScheduleEntity createScheduleForUser(final Connection connection, final ScheduleEntity scheduleEntity) throws SchedushareException;
	
	/**
	 * Gets all schedules for a user.
	 *
	 * @param connection the connection
	 * @param userId the user id
	 * @return the schedules for user
	 */
	ScheduleListEntity getSchedulesForUser(final Connection connection, final String userId) throws SchedushareException;
	
	/**
	 * Delete schedule.
	 *
	 * @param connection the connection
	 * @param scheduleId the schedule id
	 * @return the schedule entity
	 * @throws SchedushareException the schedushare exception
	 */
	ScheduleEntity deleteSchedule(final Connection connection, final int scheduleId) throws SchedushareException;
	
	/**
	 * Update schedule.
	 *
	 * @param connection the connection
	 * @param scheduleEntity the schedule entity
	 * @return the schedule entity
	 * @throws SchedushareException the schedushare exception
	 */
	ScheduleEntity updateSchedule(final Connection connection, final ScheduleEntity scheduleEntity) throws SchedushareException;
}
