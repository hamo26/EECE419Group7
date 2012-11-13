package com.schedushare.common.domain.dto;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

/**
 * Time blocks {@link RestEntity} passed between client and server as a JSON representation.
 * 
 * Simple user pojo that is structured to be serialized by JOOQ and automatically mapped.
 * Did not like the DTO generated by JOOQ so we chose to make our own.
 */
public class TimeBlocksEntity extends RestEntity {
	
	private static final String SCHEDULE_ID = "schedule-id";

	private static final String TIME_BLOCKS = "time-blocks";
	
	@SerializedName(TIME_BLOCKS)
	private Collection<TimeBlockEntity> timeBlocks;
	
	@SerializedName(SCHEDULE_ID)
	private int scheduleId;
	
	/**
	 * Constructor.
	 *
	 * @param timeBlocks the time blocks
	 */
	public TimeBlocksEntity(final int scheduleId,
			final Collection<TimeBlockEntity> timeBlocks) {
		this.scheduleId = scheduleId;
		this.timeBlocks = timeBlocks;
	}
	
	public Collection<TimeBlockEntity> getTimeBlocks() {
		return timeBlocks;
	}
	
	public int getScheduleId() {
		return scheduleId;
	}
}
