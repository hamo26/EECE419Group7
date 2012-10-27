package com.schedushare.common.domain.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.schedushare.common.guice.SchedushareCommonModule;

/**
 * Tests {@link SchedushareExceptionFactory} and its injections.
 */
public class SchedushareExceptionTest {

	@Test
	public void test() {
		TestClass testClass = Guice.createInjector(new SchedushareCommonModule()).getInstance(TestClass.class);
		String serializedJsonException = testClass.get().serializeJsonException();
		assertEquals("{\"schedushare-error\":\"test\"}", serializedJsonException);
	}
	
	/**
	 * Class to be injected using GUICE.
	 */
	private static class TestClass {

		@Inject
		private SchedushareExceptionFactory insiemeExceptionFactory;
		
		private TestClass() {
		}
		
		public SchedushareException get() {
			return this.insiemeExceptionFactory.createSchedushareException("test");
		}
	}

}
