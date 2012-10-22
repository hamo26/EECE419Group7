package com.schedushare.common.domain.dto;

import com.google.gson.annotations.SerializedName;



/**
 * Rest entity representing a Schedushare exception passed over the network.
 */
public class SchedushareExceptionEntity extends RestEntity {
	
	/** The exception. */
	@SerializedName("schedushare-error")
	private final String exception;
	
	/**
	 * Default constructor.
	 *
	 * @param exception the exception
	 */
	public SchedushareExceptionEntity(final String exception) {
		this.exception = exception;
	}
	
	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public String getException() {
		return this.exception;
	}
}
