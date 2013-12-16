package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Tests JSON parsing using Employee Service
 * 
 * @author jimmyray
 * @version 1.0
 * 
 */
// JVM Settings -Xmx512M -Dspring.profiles.active=nodb
public class EmployeeServiceJsonTest {
	private static Logger log = LoggerFactory
			.getLogger(EmployeeServiceJsonTest.class);

	private ApplicationContext ctx;
	private EmployeeService employeeService;
	private List<Employee> employees;

	@Before
	public void setup() throws Exception {
		ctx = new GenericXmlApplicationContext(
				Properties.getString("springMongoConfig.path.configFile"));
		employeeService = (EmployeeService) ctx.getBean("employeeService");
		this.employees = EmployeeShortLoader.getEmployees();
	}

	@Test
	public void testGetEmployeeJson() {
		assertEquals("Employee size was incorrect.", 161, this.employees.size());

		String json = null;

		for (Employee employee : this.employees) {
			log.debug(employee.toString());

			json = this.employeeService.getEmployeeJson(employee);

			log.debug(json);

			Employee mappedEmployee = this.employeeService
					.generateEmployeeFromJson(json);
			assertNotNull("mappedEmployee was null.", mappedEmployee);

			log.debug(mappedEmployee.toString());

			assertTrue("employee toString() did not equal.", employee
					.toString().equals(mappedEmployee.toString()));
		}
	}
}
