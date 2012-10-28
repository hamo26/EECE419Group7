package com.schedushare.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.schedushare.core.database.transaction.Transaction;
import com.schedushare.core.database.transaction.TransactionInterceptor;

/**
 * Guice bindings for AOP transactions.
 */
public class TransactionModule extends AbstractModule {

	@Override
	protected void configure() {
		TransactionInterceptor intercepter = new TransactionInterceptor();
		requestInjection(intercepter);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), intercepter);
	}

}
