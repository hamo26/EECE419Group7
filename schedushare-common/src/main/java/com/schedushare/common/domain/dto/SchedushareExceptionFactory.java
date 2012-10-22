package com.schedushare.common.domain.dto;

/**
 * Factory wired into Guice to instantiate insieme exception.
 */
public interface SchedushareExceptionFactory {
	SchedushareException createInsiemeException(String exception);
}
