package com.schedushare.common.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.schedushare.common.domain.exception.SchedushareException;
import com.schedushare.common.domain.exception.SchedushareExceptionFactory;
import com.schedushare.common.domain.rest.RestResultHandler;
import com.schedushare.common.util.JSONUtil;


/**
 * Guice dependency injection module for commons package.
 */
public class SchedushareCommonModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JSONUtil.class).annotatedWith(Names.named("jsonUtil")).to(JSONUtil.class);
		bind(RestResultHandler.class).annotatedWith(Names.named("restResultHandler")).to(RestResultHandler.class);
		install(new FactoryModuleBuilder()
		 .implement(SchedushareException.class, SchedushareException.class)
	     .build(SchedushareExceptionFactory.class));
	}

}
