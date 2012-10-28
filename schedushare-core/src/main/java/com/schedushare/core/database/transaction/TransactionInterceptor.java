package com.schedushare.core.database.transaction;

import java.lang.reflect.Method;
import java.sql.Connection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * Any method annotated with {@link Transaction} will call this method. 
 * THis method serves to roll back changes if a commit changes. Unfortunately 
 * the sql driver provided by java doesn't roll back transaction (or at least to my knowledge).
 */
public class TransactionInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Transaction annotation = method.getAnnotation(Transaction.class);
		// Make sure the intercepter was called for a transaction method
		if (annotation == null) {
			return invocation.proceed();
		}

		Connection conn = (Connection) invocation.getArguments()[0];
		try {
			conn.setAutoCommit(false);
			// Proceed with the original method's invocation.
			Object returnObj = invocation.proceed();
			// Commit if successful.
			conn.commit();
			conn.setAutoCommit(true);
			return returnObj;
		} catch (Throwable exception) {
			// Rollback on error.
			conn.rollback();
			conn.setAutoCommit(true);
			throw exception;
		}
	}
}