package com.schedushare.common.util;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.schedushare.common.domain.dto.RestEntity;
import com.schedushare.common.guice.SchedushareCommonModule;

/**
 * Tests the {@link JSONUtil}.
 */
public class JSONUtilTest extends TestCase {
	/** The Constant JSON_STRING. */
	private static final String JSON_STRING = "{\"property1\":1,\"property2\":\"2\"}";
	
	/** The injector. */
	private Injector injector;
	
	/** The json util. */
	private JSONUtil jsonUtil;

	/**
	 * Sets up common elements of the test.
	 */
	@Before
	public void setUp() {
		injector = Guice.createInjector(new SchedushareCommonModule());
		jsonUtil = injector.getInstance(TestResource.class).getJsonUtil();
		
	}
	
	/**
	 * Test deserialize representation.
	 */
	@Test
	public void testDeserializeRepresentation() {
		TestPersistentDto result = jsonUtil.deserializeRepresentation(JSON_STRING, TestPersistentDto.class);
		assertEquals(1, result.getTestProperty1());
		assertEquals("2", result.getTestProperty2());
	}

	/**
	 * Test serialize representation.
	 */
	@Test
	public void testSerializeRepresentation() {
		TestPersistentDto testPersistentDto = new TestPersistentDto(1,"2");
		String result = jsonUtil.serializeRepresentation(testPersistentDto);
		assertEquals(JSON_STRING, result);
	}
	
	/**
	 * The Class TestPersistentDto.
	 */
	private class TestPersistentDto extends RestEntity {
		
		/** The property1. */
		private int property1;
		
		/** The property2. */
		private String property2;
		
		/**
		 * Instantiates a new test persistent dto.
		 *
		 * @param property1 the property1
		 * @param property2 the property2
		 */
		public TestPersistentDto(final int property1, final String property2) {
			this.property1 = property1;
			this.property2 = property2;
		}
		
		/**
		 * Gets the test property1.
		 *
		 * @return the test property1
		 */
		public int getTestProperty1() {
			return property1;
		}
		
		/**
		 * Gets the test property2.
		 *
		 * @return the test property2
		 */
		public String getTestProperty2() {
			return property2;
		}
	}
	
	/**
	 * Class to be injected by GUICE.
	 */
	private static class TestResource {

		/** The json util. */
		private final JSONUtil jsonUtil;
	
		/**
		 * Instantiates a new test resource.
		 *
		 * @param jsonUtil the json util
		 */
		@Inject
		private TestResource(@Named("jsonUtil") final JSONUtil jsonUtil) {
			this.jsonUtil = jsonUtil;
			
		}
		
		/**
		 * Gets the json util.
		 *
		 * @return the json util
		 */
		public JSONUtil getJsonUtil() {
			return jsonUtil;
		}
	}
}
