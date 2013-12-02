package org.jimmyray.mongo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dummy Test that is run so that Maven build will compile all test classes but
 * not run any real tests.
 * 
 * @author jimmyray
 * @version 1.0
 * 
 */
public class ADummyTest {

	private static Logger log = LoggerFactory.getLogger(ADummyTest.class);

	@Test
	public void dummyTest() {
		log.debug("Dummy Test Ran.");
	}
}
