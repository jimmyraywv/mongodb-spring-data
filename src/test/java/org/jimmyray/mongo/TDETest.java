package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.security.encryption.TDEModule;
import org.jimmyray.mongo.services.EmployeeService;
import org.jimmyray.mongo.services.EncryptionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Tests TDE
 * 
 * @author jimmyray
 * @version 1.0
 */
// JVM Settings -Xmx512M -Dspring.profiles.active=local
public class TDETest {
	private static Logger log = LoggerFactory.getLogger(TDETest.class);

	private static ApplicationContext ctx;
	private MongoOperations mongoOps;
	private EmployeeService employeeService;
	private EncryptionService encryptionService;
	private List<Employee> employees;
	private Map<String, Employee> employeeMap;

	static {
		ctx = new GenericXmlApplicationContext(
				Properties.getString("springMongoConfig.path.configFile"));
	}

	@Before
	public void setup() {
		mongoOps = (MongoOperations) ctx.getBean(Properties
				.getString("springMongoConfig.bean.mongoTemplate"));

		this.employeeService = (EmployeeService) ctx.getBean(Properties
				.getString("springMongoConfig.bean.employeeService"));
		
		this.encryptionService = (EncryptionService) ctx.getBean(Properties
				.getString("springMongoConfig.bean.encryptionService"));
	}

	@Test
	public void testTDE() throws Exception {
		TDEModule tde = new TDEModule();

		this.employees = EmployeeShortLoader.getEmployees();
		this.employeeMap = new HashMap<String, Employee>();

		String encryptedData = null;
		Employee clonedEmployee = null;

		for (Employee employee : employees) {
			encryptedData = tde.encrypt(employee.getTitle());
			log.debug("Encrypted Data:  " + encryptedData);

			clonedEmployee = new Employee(employee);
			log.debug(clonedEmployee.toString());
			assertNotNull("clonedEmployee was null.", clonedEmployee);

			employeeMap.put(employee.getEmployeeId(), clonedEmployee);

			employee.setTitle(encryptedData);

			this.employeeService.saveEmployee(employee);
		}

		for (String key : this.employeeMap.keySet()) {
			log.debug("Key:  " + key);
			Employee employee = this.employeeService.findByEmployeeId(key);

			clonedEmployee = this.employeeMap.get(key);
			assertNotNull("employee was null.", employee);
			log.debug("Cloned Employee:  " + clonedEmployee.getEmployeeId()
					+ ", Title: " + clonedEmployee.getTitle());

			assertNotNull("clonedEmployee from map was null.", clonedEmployee);
			assertFalse("It does not appear that encryption worked.",
					clonedEmployee.getTitle().equals(employee.getTitle()));
			assertEquals("Titles did not match.", clonedEmployee.getTitle(),
					tde.decrypt(employee.getTitle()));

			log.debug("Employee:  " + employee.getEmployeeId() + ", Title: "
					+ employee.getTitle());
		}
	}
	
	@After
	public void tearDown() {
			log.debug("Cleaning...");
			mongoOps.dropCollection(Employee.class);
	}
}
