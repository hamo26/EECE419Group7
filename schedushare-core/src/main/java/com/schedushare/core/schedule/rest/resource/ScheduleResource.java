package com.schedushare.core.schedule.rest.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.google.inject.ImplementedBy;
import com.schedushare.core.schedule.rest.resource.impl.ScheduleResourceImpl;

/**
 * Resource used to get,put and post schedules.
 * 
 */
@ImplementedBy(ScheduleResourceImpl.class)
public interface ScheduleResource {

	/**
	 * Update schedule.
	 *
	 * @param scheduleRepresentation the schedule representation
	 * @return the string
	 */
	@Put
	public String updateSchedule(String scheduleRepresentation);
	
	/**
	 * Gets the schedule.
	 *
	 * @param scheduleRepresentation the schedule representation
	 * @return the schedule
	 */
	@Get
	public String getSchedule();
	
	/**
	 * Creates a schedule.
	 *
	 * @return the string
	 */
	@Post
	public String createSchedule(String scheduleRepresentation);
	
	/**
	 * Delete a schedule.
	 *
	 * @return the string
	 */
	@Delete
	public String deleteSchedule();
}
