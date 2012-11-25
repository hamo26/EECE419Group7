package com.schedushare.common.domain.rest;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.RestEntity;
import com.schedushare.common.domain.dto.SchedushareExceptionEntity;
import com.schedushare.common.domain.rest.RestResult.RestResultBuilder;
import com.schedushare.common.util.JSONUtil;

/**
 * All rest responses go through this handler to facilitate exception handling on the client side.
 * This class serves as a helper method to faciliate eror checking and deserialization of entities communicated
 * from the core server. 
 */
public class RestResultHandler {
	
	private final JSONUtil jsonUtil;
	
	@Inject
	public RestResultHandler(@Named("jsonUtil") final JSONUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}
	
	/**
	 * Creates the rest result for a json string representation and an expected entity.
	 *
	 * @param <E> the element type
	 * @param jsonResult the json result
	 * @param expectedType the expected type
	 * @return the rest result
	 */
	public <E extends RestEntity> RestResult<E> createRestResult(String jsonResult, Class<E> expectedType) {
		RestResult<E> restResult;
		RestResultBuilder<E> restResultBuilder = new RestResult.RestResultBuilder<E>();

		//Try exception first. TODO: use custom gson deserializer to check for this or a strict deserializer.
		//This is probably an un-neccessary check.
		SchedushareExceptionEntity schedushareExceptionDto = jsonUtil.deserializeRepresentation(jsonResult, SchedushareExceptionEntity.class);
		if (schedushareExceptionDto != null && schedushareExceptionDto.getException() != null) {
			restResult = restResultBuilder
					.setError(schedushareExceptionDto)
					.build();
		} else {
			E restResultDTO = jsonUtil.deserializeRepresentation(jsonResult, expectedType);
			restResult = restResultBuilder
					.setRestResult(restResultDTO)
					.build();
		}
		return restResult;
	}
	
	/**
	 * Creates an error RestResult for a task to return. 
	 * 
	 * TODO: Reconsider if this is the right place for this. 
	 * 		 Currently, the android client is using this method to throw task exceptions.
	 *
	 * @param error the error
	 * @return the rest result
	 */
	public <E extends RestEntity> RestResult<E> createErrorResult(String error, Class<E> expectedType) {
		RestResultBuilder<E> restResultBuilder = new RestResult.RestResultBuilder<E>();
		return restResultBuilder
				.setError(new SchedushareExceptionEntity(error))
				.build();
	}
	
	/**
	 * Creates PUT result containing an empty dto since puts dont return any representations. 
	 * 
	 * TODO: Reconsider if this is the right place for this. 
	 * 		 Currently, the android client is using this method to throw task exceptions.
	 *
	 * @param error the error
	 * @return the rest result
	 */
	public <E extends RestEntity> RestResult<E> createPutResult(Class<E> expectedType) {
		RestResultBuilder<E> restResultBuilder = new RestResult.RestResultBuilder<E>();
		return restResultBuilder
				.setRestResult(null)
				.build();
	}
	
	
}
