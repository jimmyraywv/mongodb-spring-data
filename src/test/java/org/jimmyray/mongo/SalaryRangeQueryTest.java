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
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SalaryRangeQueryTest {
	private static Logger log = LoggerFactory
			.getLogger(SalaryRangeQueryTest.class);

	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");

		EmployeeLoader.loadEmployees(true, 50000);
	}

	@Test
	public void testInRange() {
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.SALARY,
				BasicDBObjectBuilder.start("$gte", 50000).add("$lte", 100000)
						.get());

		DBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.LAST_NAME, 1);
		fields.put(EmployeeProperties.SALARY, 1);

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		DBCursor cursor = collection.find(query, fields);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 243218, cursor.count());
	}

	@Test
	public void testInRangeSpringData() {
		Query query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.SALARY).gte(50000)
				.lte(100000));
		query.fields().include(EmployeeProperties.LAST_NAME)
				.include(EmployeeProperties.SALARY)
				.exclude(EmployeeProperties.ID);

		List<Employee> employees = this.mongoOps.find(query, Employee.class);

		log.info(employees.size() + "");

		assertNotNull("List was null.", employees);
		assertEquals("List count was not correct.", 243218, employees.size());
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
