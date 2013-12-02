package org.jimmyray.mongo;

import java.util.List;

import junit.framework.TestCase;

import org.jimmyray.mongo.data.config.SpringMongoConfig;
import org.jimmyray.mongo.data.model.SpecialEmployee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class SpecialEmployeeTest extends TestCase {
	private static Logger log = LoggerFactory
			.getLogger(SpecialEmployeeTest.class);

	private static MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	@Test
	public void testSaveEmployee() throws Exception {
		SpecialEmployee employee = new SpecialEmployee();
		employee.setFirstName("Jimmy");
		employee.setLastName("Ray");
		mongoOps.save(employee);

		Query query = new Query(Criteria.where(EmployeeProperties.LAST_NAME)
				.is("Ray"));

		List<SpecialEmployee> employees = mongoOps.find(query,
				SpecialEmployee.class);
		assertNotNull("Employees list was null.", employees);
		assertTrue("Employees is empty.", employees.size() > 0);
		log.info("Employees list size = " + employees.size() + ".");

		for (SpecialEmployee emp : employees) {
			System.out.println(emp);
			assertEquals("Last name is not equal.", emp.getLastName(),
					employee.getLastName());
		}

		employees = mongoOps.find(query, SpecialEmployee.class);
		for (SpecialEmployee special : employees) {
			mongoOps.remove(special);
		}

		employees = mongoOps.find(query, SpecialEmployee.class);
		assertTrue("Employees was not empty.", employees.isEmpty());
		log.info("Employees list size = " + employees.size() + ".");

	}
}
