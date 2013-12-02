package org.jimmyray.mongo.data.loaders;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.jimmyray.mongo.data.model.Address;
import org.jimmyray.mongo.data.model.Department;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.AddressProperties;
import org.jimmyray.mongo.data.model.properties.DepartmentProperties;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.ApplicationConstants;
import org.jimmyray.mongo.framework.Filer;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Employee data factory loads data from files in list.
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class EmployeeFactory {

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(EmployeeFactory.class);

	/** The Constant COMMON_DELIMITER. */
	private static final String COMMON_DELIMITER = Properties
			.getString("employeeFactory.delimiter.comma");

	/**
	 * Instantiates a new employee factory.
	 */
	private EmployeeFactory() {
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		loadData();
	}

	/**
	 * Load data.
	 * 
	 * @return the list
	 */
	public static List<Employee> loadData() {
		Map<String, Employee> employees = null;
		Map<String, Department> depts = null;
		List<Employee> employeeList = null;

		try {

			employees = EmployeeFactory.loadEmployees();
			log.debug("Employees Size:  " + employees.size());

			depts = EmployeeFactory.loadDepts();

			EmployeeFactory.loadDeptsMgrs(depts);

			EmployeeFactory.loadMatchEmpDepts(employees, depts);

			EmployeeFactory.loadSalaryData(employees);

			EmployeeFactory.loadTitleData(employees);

			EmployeeFactory.loadAddressData(employees);

			employeeList = new ArrayList<Employee>(employees.values());
		} catch (Exception e) {
			log.error(Strings.getStackTraceAsString(e));
		}
		return employeeList;
	}

	/**
	 * Load employees.
	 * 
	 * @return the map
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws ParseException
	 *             the parse exception
	 */
	private static Map<String, Employee> loadEmployees()
			throws FileNotFoundException, ParseException {
		log.info(Properties.getString("employeeFactory.msg.loadEmployee")); //$NON-NLS-1$

		Map<String, Employee> employees = new HashMap<String, Employee>();
		Filer filer = new Filer();
		final SimpleDateFormat sdf = new SimpleDateFormat(
				Properties.getString("employeeFactory.dateMask")); //$NON-NLS-1$

		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.employeeData")); //$NON-NLS-1$

		// log.debug("Lines count: "+lines.size());

		for (String line : lines) {
			// log.debug(line);
			Employee employee = new Employee();

			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);

			employee.setEmployeeId(scanner.next());
			employee.setBirthDate(sdf.parse(scanner.next()));
			employee.setFirstName(scanner.next());
			employee.setLastName(scanner.next());
			employee.setGender(scanner.next());
			employee.setHireDate(sdf.parse(scanner.next()));
			employees.put(employee.getEmployeeId(), employee);
		}

		return employees;
	}

	/**
	 * Load depts.
	 * 
	 * @return the map
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static Map<String, Department> loadDepts()
			throws FileNotFoundException {
		Map<String, Department> depts = new HashMap<String, Department>();
		Filer filer = new Filer();

		log.info(Properties.getString("employeeFactory.msg.loadDepartment")); //$NON-NLS-1$
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.departmentData")); //$NON-NLS-1$
		for (String line : lines) {
			Department department = new Department();

			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);
			String deptId = scanner.next();
			String deptName = scanner.next();

			department.setId(deptId);
			department.setName(deptName);
			depts.put(department.getId(), department);
		}

		return depts;
	}

	/**
	 * Load depts mgrs.
	 * 
	 * @param depts
	 *            the depts
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static void loadDeptsMgrs(Map<String, Department> depts)
			throws FileNotFoundException {
		log.info(Properties.getString("employeeFactory.msg.loadDeptMgr")); //$NON-NLS-1$

		Filer filer = new Filer();
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.deptMgrData")); //$NON-NLS-1$
		for (String line : lines) {
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);
			String deptId = scanner.next();
			String manager = scanner.next();

			depts.get(deptId).setManagerId(manager);
		}
	}

	/**
	 * Load match emp depts.
	 * 
	 * @param employees
	 *            the employees
	 * @param depts
	 *            the depts
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static void loadMatchEmpDepts(Map<String, Employee> employees,
			Map<String, Department> depts) throws FileNotFoundException {

		log.info(Properties.getString("employeeFactory.msg.employeeDept")); //$NON-NLS-1$

		Filer filer = new Filer();
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.empDeptData")); //$NON-NLS-1$
		for (String line : lines) {
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);
			String empId = scanner.next();
			String deptId = scanner.next();

			Employee employee = employees.get(empId);
			if (null != employee) {
				employee.setDepartment(depts.get(deptId));
			}
		}
	}

	/**
	 * Load salary data.
	 * 
	 * @param employees
	 *            the employees
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static void loadSalaryData(Map<String, Employee> employees)
			throws FileNotFoundException {
		log.info(Properties.getString("employeeFactory.msg.loadSalary")); //$NON-NLS-1$

		Filer filer = new Filer();
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.salaryData")); //$NON-NLS-1$
		for (String line : lines) {
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);
			String empId = scanner.next();
			String salary = scanner.next();

			Employee employee = employees.get(empId);
			if (null != employee) {
				employee.setSalary(Integer.parseInt(salary));
			}
		}
	}

	/**
	 * Load title data.
	 * 
	 * @param employees
	 *            the employees
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static void loadTitleData(Map<String, Employee> employees)
			throws FileNotFoundException {
		log.info(Properties.getString("employeeFactory.msg.loadTitles")); //$NON-NLS-1$

		Filer filer = new Filer();
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.titleData")); //$NON-NLS-1$
		for (String line : lines) {
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);
			String empId = scanner.next();
			String title = scanner.next();

			Employee employee = employees.get(empId);
			if (null != employee) {
				employee.setTitle(title);
			}
		}
	}

	/**
	 * Load address data.
	 * 
	 * @param employees
	 *            the employees
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private static void loadAddressData(Map<String, Employee> employees)
			throws FileNotFoundException {

		log.info(Properties.getString("employeeFactory.msg.loadAddress")); //$NON-NLS-1$

		List<Address> addresses = new ArrayList<Address>();
		Filer filer = new Filer();
		List<String> lines = filer.readFileAsResource(Properties
				.getString("employeeFactory.path.addressData")); //$NON-NLS-1$
		for (String line : lines) {
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(EmployeeFactory.COMMON_DELIMITER);
			String street = scanner.next();
			String city = scanner.next();
			String county = scanner.next();
			String state = scanner.next();
			String zip = scanner.next();

			Address address = new Address();
			address.setId(ApplicationConstants.ADDRESS_ID);
			address.setAddressLine1(street);
			address.setCity(city);
			address.setCounty(county);
			address.setState(state);
			address.setZipCode(zip);

			addresses.add(address);
		}

		int index = 0;
		for (Entry<String, Employee> entry : employees.entrySet()) {
			entry.getValue().setAddress(addresses.get(index));
			index++;
		}
	}

	/**
	 * Gets the map from an Employee object.
	 * 
	 * @param employee
	 *            the employee
	 * @return the map
	 */
	public static Map<String, Object> getMap(Employee employee) {
		Map<String, Object> employeeMap = new LinkedHashMap<String, Object>();
		Map<String, Object> addressMap = new LinkedHashMap<String, Object>();
		Map<String, Object> departmentMap = new LinkedHashMap<String, Object>();

		employeeMap
				.put(EmployeeProperties.CLASS, employee.getClass().getName());
		employeeMap.put(EmployeeProperties.EMPLOYEE_ID,
				employee.getEmployeeId());
		employeeMap.put(EmployeeProperties.HIRE_DATE, employee.getHireDate());
		employeeMap.put(EmployeeProperties.TITLE, employee.getTitle());
		employeeMap.put(EmployeeProperties.SALARY, employee.getSalary());
		employeeMap.put(EmployeeProperties.LAST_NAME, employee.getLastName());
		employeeMap.put(EmployeeProperties.FIRST_NAME, employee.getFirstName());
		employeeMap.put(EmployeeProperties.GENDER, employee.getGender());
		employeeMap.put(EmployeeProperties.BIRTH_DATE, employee.getBirthDate());

		addressMap.put(AddressProperties.LINE1, employee.getAddress()
				.getAddressLine1());
		if (null != employee.getAddress().getAddressLine2()) {
			addressMap.put(AddressProperties.LINE2, employee.getAddress()
					.getAddressLine2());
		}
		addressMap.put(AddressProperties.CITY, employee.getAddress().getCity());
		addressMap.put(AddressProperties.COUNTY, employee.getAddress()
				.getCounty());
		addressMap.put(AddressProperties.STATE, employee.getAddress()
				.getState());
		addressMap.put(AddressProperties.ZIP, employee.getAddress()
				.getZipCode());

		employeeMap.put(EmployeeProperties.ADDRESS, addressMap);

		departmentMap.put(DepartmentProperties.ID, employee.getDepartment()
				.getId());
		departmentMap.put(DepartmentProperties.NAME, employee.getDepartment()
				.getName());
		departmentMap.put(DepartmentProperties.MANAGER_ID, employee
				.getDepartment().getManagerId());

		employeeMap.put(EmployeeProperties.DEPARTMENT, departmentMap);

		return employeeMap;
	}
}
