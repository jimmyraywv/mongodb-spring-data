package org.jimmyray.mongo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jimmyray.mongo.data.config.SpringMongoConfig;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoTemplateTest {
	private static Logger log = LoggerFactory
			.getLogger(MongoTemplateTest.class);

	private static MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	@Test
	public void testSaveEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setFirstName("Jimmy");
		employee.setLastName("Ray");
		mongoOps.save(employee);

		Query query = new Query(Criteria.where(EmployeeProperties.LAST_NAME)
				.is("Ray"));

		List<Employee> employees = mongoOps.find(query, Employee.class);
		assertNotNull("Employees list was null.", employees);
		assertTrue("Employees is empty.", !employees.isEmpty());
		log.info("Employees list size = " + employees.size() + ".");

		for (Employee emp : employees) {
			log.info(emp.toString());
			org.junit.Assert.assertEquals("Last name is not equal.",
					emp.getLastName(), employee.getLastName());
		}

		// mongoOps.findAndRemove(query, Employee.class, "employees");
		/*
		 * employees = mongoOps.find(query, Employee.class);
		 * assertNotNull("Employees list was null.", employees);
		 * assertTrue("Employees is not empty.", employees.size() == 0);
		 * log.info("Employees list size = " + employees.size() + ".");
		 */

	}

	@After
	public void cleanDb() {
		log.info("Cleaning MongoDB, removing \"employees\" collection...");
		mongoOps.dropCollection(Employee.class);
	}
}
