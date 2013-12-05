package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class SalaryGtQueryTest {
	private static Logger log = LoggerFactory
			.getLogger(SalaryGtQueryTest.class);

	private static MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");

		EmployeeLoader.loadEmployees(SpringBeanFactory.getContext(), true,
				50000);
	}

	@Test
	public void testGt() {
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.SALARY, new BasicDBObject("$gt", 50000));

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		DBCursor cursor = collection.find(query);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 262192, cursor.count());
	}

	@Test
	public void testGtSpringData() {
		Query query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.SALARY).gt(50000));
		List<Employee> employees = mongoOps.find(query, Employee.class);

		log.info(employees.size() + "");

		assertNotNull("List was null.", employees);
		assertEquals("List count was not correct.", 262192, employees.size());
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
