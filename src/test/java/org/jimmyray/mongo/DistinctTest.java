package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.DBCollection;

public class DistinctTest {
	private static Logger log = LoggerFactory.getLogger(DistinctTest.class);

	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");
		EmployeeLoader.loadEmployees(SpringBeanFactory.getContext(), true,
				50000);
	}

	@Test
	public void testDistinct() {
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		List<String> lastNames = collection
				.distinct(EmployeeProperties.LAST_NAME);

		assertEquals("Distinct lname count was incorrect.", 1637,
				lastNames.size());
	}

	public void testDistinctSpringData() {
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		List<String> lastNames = collection
				.distinct(EmployeeProperties.LAST_NAME);

		assertEquals("Distinct lname count was incorrect.", 1637,
				lastNames.size());
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
