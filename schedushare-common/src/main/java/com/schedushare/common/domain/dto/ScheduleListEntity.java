package com.schedushare.common.domain.dto;

import java.util.Collection;

/**
 * Simple schedule list pojo structured to be serialized by jooq and mapped to appropriate fields in json representation.
 */
public class ScheduleListEntity extends RestEntity {

	private final Collection<ScheduleEntity> scheduleList;
	
	/**
	 * Default Constructor.
	 *
	 * @param scheduleList the schedule list
	 */
	public ScheduleListEntity(final Collection<ScheduleEntity> scheduleList) {
		this.scheduleList = scheduleList;
		
	}

	/**
	 * Gets the schedule list.
	 *
	 * @return the schedule list
	 */
	public Collection<ScheduleEntity> getScheduleList() {
		return scheduleList;
	}
}
