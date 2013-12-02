package org.jimmyray.mongo.data.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.BaseProperties;
import org.jimmyray.mongo.data.model.properties.DepartmentProperties;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader class for Dept. data
 * @author jimmyray
 *
 */
public class DepartmentLoader {
	private static Logger log = LoggerFactory.getLogger(DepartmentLoader.class);

	private DepartmentLoader() {

	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, Map<String, Object>> deptMap = DepartmentLoader
				.getDeptMap();

		for (Entry entry : deptMap.entrySet()) {
			log.info(entry.getKey().toString());
			log.info(entry.getValue().toString());
		}

	}

	/**
	 * Returns a map of employees in a dept map.
	 * @return
	 */
	public static Map<String, Map<String, Object>> getDeptMap() {

		List<Employee> employees = EmployeeFactory.loadData();
		Map<String, Map<String, Object>> deptMap = new HashMap<String, Map<String, Object>>();

		for (Employee employee : employees) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(BaseProperties.ID, employee.getDepartment().getId());
			map.put(DepartmentProperties.NAME, employee.getDepartment()
					.getName());
			map.put(DepartmentProperties.MANAGER_ID, employee.getDepartment()
					.getManagerId());
			map.put("employees", new ArrayList<Map<String, Object>>());
			deptMap.put(employee.getDepartment().getId(), map);
		}

		for (Employee employee : employees) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(EmployeeProperties.EMPLOYEE_ID, employee.getEmployeeId());
			map.put(EmployeeProperties.LAST_NAME, employee.getLastName());
			map.put(EmployeeProperties.FIRST_NAME, employee.getFirstName());
			map.put(EmployeeProperties.HIRE_DATE, employee.getHireDate());
			map.put(EmployeeProperties.TITLE, employee.getTitle());
			map.put(EmployeeProperties.SALARY, employee.getSalary());
			((List<Map<String, Object>>) deptMap.get(
					employee.getDepartment().getId()).get("employees"))
					.add(map);

		}

		return deptMap;
	}
}
