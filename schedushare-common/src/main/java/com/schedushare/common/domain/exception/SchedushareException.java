package com.schedushare.common.domain.exception;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.SchedushareExceptionEntity;
import com.schedushare.common.util.JSONUtil;


/**
 * Wraps a {@link SchedushareExceptionEntity} for convinience purposes. 
 */
public class SchedushareException extends Exception {

	
	private static final long serialVersionUID = -3722788484561737019L;
	
	private final SchedushareExceptionEntity exception;

	private final JSONUtil jsonUtil;
	
	/**
	 * Default constructor.
	 *
	 * @param jsonUtil the json util
	 * @param exception the exception
	 */
	@Inject
	public SchedushareException (@Named("jsonUtil") final JSONUtil jsonUtil, 
			@Assisted final String exception) {
		this.jsonUtil = jsonUtil;
		this.exception = new SchedushareExceptionEntity(exception);
	}
	
	/**
	 * Serialize the json exception.
	 *
	 * @return the string
	 */
	public String serializeJsonException() {
		return jsonUtil.serializeRepresentation(this.exception);
	}

}
