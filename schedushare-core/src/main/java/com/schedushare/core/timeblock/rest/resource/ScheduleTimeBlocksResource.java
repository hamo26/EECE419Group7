package com.schedushare.core.timeblock.rest.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface ScheduleTimeBlocksResource {

	/**
	 * Gets the time blocks for a schedule.
	 *
	 * @return the time blocks for a schedule
	 */
	@Get
	String getTimeBlocksForSchedule();
	
	@Put
	String updateTimeBlocksForDay(String timeBlocksRepresentation);
	
}
