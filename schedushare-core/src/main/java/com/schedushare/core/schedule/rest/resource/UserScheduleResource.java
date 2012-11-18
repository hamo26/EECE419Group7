package com.schedushare.core.schedule.rest.resource;

import org.restlet.resource.Get;

import com.google.inject.ImplementedBy;
import com.schedushare.core.schedule.rest.resource.impl.UserScheduleResourceImpl;

/**
 * Resource used to get and create schedules for a user.
 */
@ImplementedBy(UserScheduleResourceImpl.class)
public interface UserScheduleResource {

	/**
	 * Gets the schedules for a user.
	 *
	 * @param scheduleRepresentation the schedule representation
	 * @return the schedule
	 */
	@Get
	String getSchedulesForUser();
}
