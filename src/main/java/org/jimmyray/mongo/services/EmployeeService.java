package org.jimmyray.mongo.services;

import org.jimmyray.mongo.data.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * EmployeeService interface defines employee service contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface EmployeeService {
	Employee saveEmployee(Employee employee);

	void saveEmployees(List<Employee> employees);

	List<Employee> listEmployees();

	Employee getEmployeeById(String employeeId);

	List<Employee> findAll();

	Page<Employee> findAllWithPages(int pageStart, int pageSize,
			Sort.Direction sortDirection, String sortField);

	List<Employee> findByLastName(String lastName);

	List<Employee> findByFirstName(String firstName);

	List<Employee> findByGender(String gender);
	
	Employee findByEmployeeId(String employeeId);

	//List<Employee> queryByLastNameAndDepartment(String lastName, String deptName);

	void bulkInsert(List<Employee> employees, int batchSize);

	void deleteEmployee(Employee employee);

	void deleteAll();
	
	String getEmployeeJson(Employee employee);
	
	Employee generateEmployeeFromJson(String json);
}
