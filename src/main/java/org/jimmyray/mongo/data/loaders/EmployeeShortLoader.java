package org.jimmyray.mongo.data.loaders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jimmyray.mongo.data.model.Address;
import org.jimmyray.mongo.data.model.Department;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.framework.Filer;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.Strings;
import org.jimmyray.mongo.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Loads small subset of employee data into MongoDB
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class EmployeeShortLoader {
	private static Logger log = LoggerFactory
			.getLogger(EmployeeShortLoader.class);

	private static final String COMMON_DELIMITER = Properties
			.getString("employeeFactory.delimiter.comma");

	private EmployeeShortLoader() {
	}

	public static void main(String[] args) throws Exception {
		log.info(Properties
				.getString("springMongoConfig.msg.startingContainer")); //$NON-NLS-1$
		ApplicationContext ctx = new GenericXmlApplicationContext(
				Properties.getString("springMongoConfig.path.configFile")); //$NON-NLS-1$

		EmployeeService employeeService = (EmployeeService) ctx
				.getBean(Properties
						.getString("springMongoConfig.bean.employeeService")); //$NON-NLS-1$

		List<Employee> employees = EmployeeShortLoader.getEmployees();

		log.info(Properties.getString("employeeShortLoader.msg.saving")); //$NON-NLS-1$
		employeeService.saveEmployees(employees);
		log.info(Properties.getString("mongoEmployeeLoader.msg.complete")); //$NON-NLS-1$
	}

	public static List<Employee> getEmployees() throws Exception {
		log.info(Properties.getString("employeeFactory.msg.loadEmployee")); //$NON-NLS-1$

		Filer filer = new Filer();
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeShortLoader.path.employeeDataLonger")); //$NON-NLS-1$

		List<Employee> employees = EmployeeShortLoader.scanEmployees(lines);

		return employees;
	}

	public static List<Employee> scanEmployees(List<String> lines) {
		List<Employee> employees = new ArrayList<Employee>();
		SimpleDateFormat sdf = new SimpleDateFormat(
				Properties.getString("employeeFactory.dateMask")); //$NON-NLS-1$

		for (String fileLine : lines) {
			try {
				Employee employee = new Employee();
				Scanner line = new Scanner(fileLine);
				line.useDelimiter(Properties
						.getString("employeeShortLoader.delimiter.pipe")); //$NON-NLS-1$
				line.next();
				line.next();
				employee.setEmployeeId(line.next());
				employee.setHireDate(sdf.parse(line.next()));
				employee.setFirstName(line.next());
				employee.setGender(line.next());
				employee.setLastName(line.next());
				employee.setTitle(line.next());
				employee.setSalary(Integer.parseInt(line.next()));
				Department dept = new Department();
				Scanner inline = new Scanner(line.next());
				inline.useDelimiter(EmployeeShortLoader.COMMON_DELIMITER);
				dept.setId(inline.next());
				dept.setName(inline.next());
				dept.setManagerId(inline.next());
				employee.setDepartment(dept);
				employee.setBirthDate(sdf.parse(line.next()));
				Address address = new Address();
				address.setId("Home");
				inline = new Scanner(line.next());
				inline.useDelimiter(EmployeeShortLoader.COMMON_DELIMITER);
				address.setAddressLine1(inline.next());
				inline.next();
				address.setCity(inline.next());
				address.setCounty(inline.next());
				address.setState(inline.next());
				address.setZipCode(inline.next());
				employee.setAddress(address);

				employees.add(employee);
			} catch (ParseException pe) {
				log.error(Strings.getStackTraceAsString(pe));
			}
		}

		return employees;
	}
}
