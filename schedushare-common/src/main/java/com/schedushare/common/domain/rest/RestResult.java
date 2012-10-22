package com.schedushare.common.domain.rest;

import com.schedushare.common.domain.dto.RestEntity;
import com.schedushare.common.domain.dto.SchedushareExceptionEntity;

/**
 * This class wraps a dto returned and signals whether the result of a REsT operation 
 * was successful or not. If it wasn't we provide the error, otherwise we provide the actual 
 * {@link RestEntity} result.
 *
 * @param <T> the generic type which extends {@link RestEntity}.
 */
public class RestResult<T extends RestEntity> {

	private final Boolean isSuccessful;
	
	private final SchedushareExceptionEntity schedushareExceptionDto;
	
	private final T restResultDto;

	/**
	 * Default constructor.
	 *
	 * @param isSuccessful the is successful
	 * @param restResultDto the rest result dto
	 * @param insiemeExceptionDto the insieme exception dto
	 */
	private RestResult(final Boolean isSuccessful, 
					   final T restResultDto, 
					   final SchedushareExceptionEntity insiemeExceptionDto) {
		this.isSuccessful = isSuccessful;
		this.restResultDto = restResultDto;
		this.schedushareExceptionDto = insiemeExceptionDto;
	}
	
	public Boolean isSuccessfull() {
		return this.isSuccessful == Boolean.TRUE;
	}
	
	public Boolean isFailure() {
		return this.isSuccessful == Boolean.FALSE;
	}
	
	public SchedushareExceptionEntity getError() {
		return this.schedushareExceptionDto;
	}
	
	public T getRestResult() {
		return this.restResultDto;
	}
	
	/**
	 * Inner builder class used to create a {@link RestResult}.
	 * 
	 * Look at the builder pattern for more information on why I chose this 
	 * approach.
	 *
	 * @param <T> the generic type
	 */
	public static final class RestResultBuilder<T extends RestEntity> {
		
		private Boolean isSuccessful;
		
		private SchedushareExceptionEntity schedushareExceptionDto;
		
		private T restResultDto;

		public RestResultBuilder<T> setRestResult(T restResponseDto) {
			this.restResultDto = restResponseDto;
			this.isSuccessful = Boolean.TRUE;
			return this;
		}
		
		public RestResultBuilder<T> setError(SchedushareExceptionEntity insiemeExceptionDto) {
			this.schedushareExceptionDto = insiemeExceptionDto;
			this.isSuccessful = Boolean.FALSE;
			return this;
		}
		
		/**
		 * Builds the {@link RestResult} and ensures paramaters are used to build are valid.
		 *
		 * @return the rest result
		 */
		public RestResult<T> build() {
			assert this.isSuccessful != null : "rest result must be either successful or not.";
			if (this.isSuccessful) {
				assert this.schedushareExceptionDto == null : "no exception should have occured if rest result succeeded";
				assert this.restResultDto != null : "requests that succeed should have resultDtos.";
			} else {
				assert this.schedushareExceptionDto != null : "exception should have occured rest result failed";
				assert this.restResultDto == null : "requests that do not succeed should not have resultDtos.";
			}
			return new RestResult<T>(this.isSuccessful, this.restResultDto, this.schedushareExceptionDto);
		}
	}
}
