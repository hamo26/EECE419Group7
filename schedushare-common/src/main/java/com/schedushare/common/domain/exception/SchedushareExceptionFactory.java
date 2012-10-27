package com.schedushare.common.domain.exception;


/**
 * Factory wired into Guice to instantiate insieme exception.
 */
public interface SchedushareExceptionFactory {
	SchedushareException createInsiemeException(String exception);
}
