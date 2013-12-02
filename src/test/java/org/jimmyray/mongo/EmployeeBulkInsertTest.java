package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeFactory;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.services.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

public class EmployeeBulkInsertTest {
	private static Logger log = LoggerFactory
			.getLogger(EmployeeBulkInsertTest.class);

	private ApplicationContext ctx;
	private MongoOperations mongoOps;
	private EmployeeService employeeService;
	private List<Employee> employees;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		employeeService = (EmployeeService) ctx.getBean("employeeService");
	}

	@Test
	public void testBulkInsert() {
		mongoOps.dropCollection(Employee.class);
		employees = EmployeeFactory.loadData();
		long startTime = System.currentTimeMillis();
		employeeService.bulkInsert(employees, 50000);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("BulkInsert time: " + duration + "ms");
		employees = employeeService.findAll();
		assertEquals("Employee count incorrect.", 300024, employees.size());
	}

	@After
	public void cleanDb() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
