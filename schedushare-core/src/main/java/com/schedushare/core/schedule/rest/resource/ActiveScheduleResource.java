package com.schedushare.core.schedule.rest.resource;

import com.google.inject.ImplementedBy;
import com.schedushare.core.schedule.rest.resource.impl.ActiveScheduleResourceImpl;


/**
 * Resource to get an active schedule for a user.
 */
@ImplementedBy(ActiveScheduleResourceImpl.class)
public interface ActiveScheduleResource {

	/**
	 * Gets the active schedule for a user.
	 *
	 * @return the active schedule for user
	 */
	String getActiveScheduleForUser();
}
