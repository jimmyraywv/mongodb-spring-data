package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class FindAndTest {
	private static Logger log = LoggerFactory.getLogger(FindAndTest.class);

	private ApplicationContext ctx;
	private MongoOperations mongoOps;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");

		try {
			EmployeeShortLoader.main(null);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@Test
	public void testFindAndModify() {
		Query query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.EMPLOYEE_ID).is(
				"28241"));

		Update update = new Update();
		update.set(EmployeeProperties.FIRST_NAME, "John").set(
				EmployeeProperties.LAST_NAME, "Smith");

		this.mongoOps.findAndModify(query, update, Employee.class);

		Employee employee = this.mongoOps.findOne(query, Employee.class);

		assertNotNull("Employee was null", employee);
		assertEquals("Last name was incorrect.", "Smith",
				employee.getLastName());
		assertEquals("First name was incorrect.", "John",
				employee.getFirstName());
	}

	@Test
	public void testFindAndRemove() {
		Query query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.EMPLOYEE_ID).is(
				"28241"));

		this.mongoOps.findAndRemove(query, Employee.class);

		Employee employee = this.mongoOps.findOne(query, Employee.class);

		assertNull("Employee was not null", employee);
	}

	@After
	public void cleanDb() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
