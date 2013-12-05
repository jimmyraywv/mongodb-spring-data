package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.jimmyray.mongo.services.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UpdateTest {
	private static Logger log = LoggerFactory.getLogger(UpdateTest.class);

	private MongoOperations mongoOps;
	private List<Employee> employees;
	private EmployeeService employeeService;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");
		employeeService = (EmployeeService) SpringBeanFactory
				.getBean("employeeService");
		EmployeeLoader.loadEmployees(SpringBeanFactory.getContext(), true,
				50000);

		long startTime = System.currentTimeMillis();
		employees = mongoOps.findAll(Employee.class);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("Employee find time: " + duration + "ms");
	}

	@Test
	public void updateDob() {
		assertNotNull("employees was null.", employees);
		assertEquals("employees count was not correct.", 300024,
				employees.size());

		// Save entire employee Document
		Date now = new Date();

		long startTime = System.currentTimeMillis();
		for (Employee employee : employees) {
			employee.setBirthDate(now);
			this.employeeService.saveEmployee(employee);
		}
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("Employee save time: " + duration + "ms");

		// Update only, via mongo Java driver
		startTime = System.currentTimeMillis();
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		assertNotNull("employees was null.", collection);
		assertEquals("employees count was not correct.", 300024,
				collection.count());

		DBObject query = new BasicDBObject();
		DBObject update = new BasicDBObject();
		update.put("$set",
				new BasicDBObject(EmployeeProperties.BIRTH_DATE, now));

		now = new Date();
		collection.updateMulti(query, update);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		log.info("Employee update time: " + duration + "ms");

		// Update only, via Spring Data
		startTime = System.currentTimeMillis();
		now = new Date();
		Query springQuery = new Query();
		// springQuery.addCriteria(Criteria.where(EmployeeProperties.LAST_NAME).is("Smith"));
		springQuery.fields().include(EmployeeProperties.BIRTH_DATE);
		Update springUpdate = new Update();
		springUpdate.set(EmployeeProperties.BIRTH_DATE, now);

		mongoOps.updateMulti(springQuery, springUpdate, Employee.class);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		log.info("Employee update time (via Spring Data): " + duration + "ms");
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
