package com.schedushare.core.schedule.rest.resource;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.google.inject.ImplementedBy;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.core.schedule.rest.resource.impl.UserScheduleResourceImpl;

/**
 * Resource used to get,put and post schedules associated with users.
 * 
 */
@ImplementedBy(UserScheduleResourceImpl.class)
public interface UserScheduleResource {

	/**
	 * Creates the schedule.
	 *
	 * @param scheduleRepresentation the schedule representation
	 * @return the string
	 * @throws SchedushareException the schedushare exception
	 */
	@Put
	public String createSchedule(String scheduleRepresentation) throws SchedushareException;
	
	/**
	 * Update schedule.
	 *
	 * @param scheduleRepresentation the schedule representation
	 * @return the string
	 * @throws SchedushareException the schedushare exception
	 */
	@Post
	public String updateSchedule(String scheduleRepresentation) throws SchedushareException;
	
	/**
	 * Gets the schedule.
	 *
	 * @param scheduleRepresentation the schedule representation
	 * @return the schedule
	 * @throws SchedushareException the schedushare exception
	 */
	@Get
	public String getSchedule(String scheduleRepresentation) throws SchedushareException;
}
