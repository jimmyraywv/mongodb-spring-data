package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeLoader;
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


//JVM Settings -Xmx512M -Dspring.profiles.active=local
public class EmployeeServiceTest {
	private static Logger log = LoggerFactory
			.getLogger(EmployeeServiceTest.class);

	private ApplicationContext ctx;
	private MongoOperations mongoOps;
	private EmployeeService employeeService;
	private List<Employee> employees;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		employeeService = (EmployeeService) ctx.getBean("employeeService");
		EmployeeLoader.loadEmployees(ctx, true, 50000);
	}

	@Test
	public void testEmployeeService() throws Exception {
		employees = employeeService.findByLastName("Smith");
		assertTrue("Employees list is not empty.", employees.isEmpty());

		employees = employeeService.findByLastName("Baby");
		assertEquals("Employees list count is not correct.", 197,
				employees.size());

		for (Employee employee : employees) {
			employee.setGender("TEST");
			employeeService.saveEmployee(employee);
		}

		employees = employeeService.findByGender("TEST");
		assertEquals("Employees list count is not correct.", 197,
				employees.size());

		for (Employee employee : employees) {
			assertEquals("Middle name was not correct.", "TEST",
					employee.getGender());
		}

		for (Employee employee : employees) {
			employeeService.deleteEmployee(employee);
		}

		employees = employeeService.findByGender("TEST");
		assertTrue("Employees is not empty.", employees.isEmpty());

		employees = employeeService.queryByLastNameAndDepartment("Chenney",
				"Sales");
		assertEquals("Employees list size is not correct.", 29,
				employees.size());

		for (Employee employee : employees) {
			log.info(employee.toString());
		}

		employeeService.deleteAll();
		employees = employeeService.findAll();
		assertTrue("Employees list is not empty.", employees.isEmpty());

	}

	@After
	public void cleanDb() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}

}
