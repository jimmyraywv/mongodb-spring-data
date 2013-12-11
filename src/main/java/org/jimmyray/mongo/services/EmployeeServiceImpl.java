package org.jimmyray.mongo.services;

import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Implementation of EmployeeService contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	@Override
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public void saveEmployees(List<Employee> employees) {
		for (Employee employee : employees) {
			this.saveEmployee(employee);
		}
	}

	@Override
	public List<Employee> listEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(String employeeId) {
		return employeeRepository.findByEmployeeId(employeeId);
	}

	@Override
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Override
	public Page<Employee> findAllWithPages(int pageStart, int pageSize,
			Sort.Direction sortDirection, String sortField) {
		PageRequest pageRequest = new PageRequest(pageStart, pageSize,
				new Sort(Sort.Direction.ASC, sortField));
		return this.employeeRepository.findAll(pageRequest);
	}

	@Override
	public List<Employee> findByLastName(String lastName) {
		return employeeRepository.findByLastName(lastName);
	}

	@Override
	public List<Employee> findByFirstName(String firstName) {
		return employeeRepository.findByFirstName(firstName);
	}

	@Override
	public List<Employee> findByGender(String gender) {
		return employeeRepository.findByGender(gender);
	}

	@Override
	public Employee findByEmployeeId(String employeeId) {
		return employeeRepository.findByEmployeeId(employeeId);
	}

	@Override
	public List<Employee> queryByLastNameAndDepartment(String lastName,
			String deptName) {
		return employeeRepository.queryByEmployeeLastNameAndDepartmentName(
				lastName, deptName);
	}

	@Override
	public void deleteEmployee(Employee employee) {
		this.employeeRepository.delete(employee);
	}

	@Override
	public void deleteAll() {
		this.employeeRepository.deleteAll();
	}

	@Override
	public void bulkInsert(List<Employee> employees, int batchSize) {
		this.employeeRepository.bulkInsert(employees, batchSize);
	}

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
}
