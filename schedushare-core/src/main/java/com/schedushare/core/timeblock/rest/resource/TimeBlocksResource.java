package com.schedushare.core.timeblock.rest.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.google.inject.ImplementedBy;
import com.schedushare.core.timeblock.rest.resource.impl.TimeBlocksResourceImpl;

/**
 * Resource for timeblocks.
 */
@ImplementedBy(TimeBlocksResourceImpl.class)
public interface TimeBlocksResource {

	/**
	 * Gets a time block.
	 *
	 * @param timeBlockId the time block id
	 * @return the time block
	 */
	@Get
	public String getTimeBlock();
	
	/**
	 * Creates the time blocks.
	 *
	 * @param timeBlocksRepresentation the time blocks representation
	 * @return the string
	 */
	@Post
	public String createTimeBlocks(String timeBlocksRepresentation);
	
	/**
	 * Update time blocks.
	 *
	 * @param timeBlocksRepresentation the time blocks representation
	 * @return the string
	 */
	@Put
	public String updateTimeBlocks(String timeBlocksRepresentation);
	
	/**
	 * Delete time blocks.
	 *
	 * @return json representation of deleted time block.
	 */
	@Delete
	public String deleteTimeBlocks();
}