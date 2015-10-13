package org.jimmyray.mongo.services;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger log = LoggerFactory
			.getLogger(EmployeeServiceImpl.class);

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

	@Override
	public String getEmployeeJson(Employee employee) {
		String json = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(DateFormat.getDateInstance());
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			json = mapper.writeValueAsString(employee);
		} catch (JsonGenerationException jge) {
			log.error(jge.getLocalizedMessage());
		} catch (JsonMappingException jme) {
			log.error(jme.getLocalizedMessage());
		} catch (IOException ioe) {
			log.error(ioe.getLocalizedMessage());
		}

		return json;
	}

	@Override
	public Employee generateEmployeeFromJson(String json) {
		Employee employee = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			employee = mapper.readValue(json, Employee.class);
		} catch (JsonParseException jpe) {
			log.error(jpe.getLocalizedMessage());
		} catch (JsonMappingException jme) {
			log.error(jme.getLocalizedMessage());
		} catch (IOException ioe) {
			log.error(ioe.getLocalizedMessage());
		}

		return employee;
	}
}
