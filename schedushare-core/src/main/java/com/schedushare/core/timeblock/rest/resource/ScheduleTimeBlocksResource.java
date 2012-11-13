package com.schedushare.core.timeblock.rest.resource;

import org.restlet.resource.Get;

public interface ScheduleTimeBlocksResource {

	/**
	 * Gets the time blocks for a schedule.
	 *
	 * @return the time blocks for a schedule
	 */
	@Get
	public String getTimeBlocksForSchedule();
}
