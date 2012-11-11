package com.schedushare.core.timeblocks.service;

import java.sql.Connection;

import com.google.inject.ImplementedBy;
import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.core.timeblocks.service.impl.TimeBlocksServiceImpl;

@ImplementedBy(TimeBlocksServiceImpl.class)
public interface TimeBlocksService {

	/**
	 * Gets the time block.
	 *
	 * @param connection the connection
	 * @param timeBlockId the time block id
	 * @return the time block
	 */
	public TimeBlockEntity getTimeBlock(Connection connection, int timeBlockId) throws SchedushareException;
	
	/**
	 * Gets the time blocks for schedule.
	 *
	 * @param connection the connection
	 * @param scheduleId the schedule id
	 * @return the time blocks for schedule
	 */
	public TimeBlocksEntity getTimeBlocksForSchedule(Connection connection, int scheduleId) throws SchedushareException;
	
	/**
	 * Creates a collection of time blocks.
	 *
	 * @param connection the connection
	 * @param scheduleId the schedule id
	 * @param timeBlocks the time blocks
	 * @return the time blocks entity
	 * @throws SchedushareException the schedushare exception
	 */
	public TimeBlocksEntity createTimeBlocks(Connection connection, int scheduleId, TimeBlocksEntity timeBlocks) throws SchedushareException;
	
	/**
	 * Update time block.
	 *
	 * @param connection the connection
	 * @param timeBlockId the time block id
	 * @return the time block entity
	 */
	public TimeBlockEntity updateTimeBlock(Connection connection, int timeBlockId) throws SchedushareException;
	
	/**
	 * Delete time block.
	 *
	 * @param connection the connection
	 * @param timeBlockId the time block id
	 * @return the time block entity
	 */
	public TimeBlockEntity deleteTimeBlock(Connection connection, int timeBlockId) throws SchedushareException;
}
