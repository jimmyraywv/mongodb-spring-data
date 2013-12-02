package org.jimmyray.mongo.services;

import java.util.ArrayList;
import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.data.repository.EmployeeRepository;
import org.jimmyray.mongo.data.transformers.EmployeeTransformer;
import org.jimmyray.mongo.framework.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

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
	private Mongo mongo;
	private DB db;

	public void init() {
		if (mongo != null) {
			db = mongo.getDB(Properties.getString("mongodb.db"));
		}
	}

	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public void saveEmployees(List<Employee> employees) {
		for (Employee employee : employees) {
			this.saveEmployee(employee);
		}
	}

	public List<Employee> listEmployees() {
		return employeeRepository.findAll();
	}

	public Employee getEmployeeById(String employeeId) {
		return employeeRepository.findByEmployeeId(employeeId);
	}

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Page<Employee> findAllWithPages(int pageStart, int pageSize,
			Sort.Direction sortDirection, String sortField) {
		PageRequest pageRequest = new PageRequest(pageStart, pageSize,
				new Sort(Sort.Direction.ASC, sortField));
		return this.employeeRepository.findAll(pageRequest);
	}

	public List<Employee> findByLastName(String lastName) {
		return employeeRepository.findByLastName(lastName);
	}

	public List<Employee> findByFirstName(String firstName) {
		return employeeRepository.findByFirstName(firstName);
	}

	public List<Employee> findByGender(String gender) {
		return employeeRepository.findByGender(gender);
	}

	public List<Employee> queryByLastNameAndDepartment(String lastName,
			String deptName) {
		return employeeRepository.queryByEmployeeLastNameAndDepartmentName(
				lastName, deptName);
	}

	public void deleteEmployee(Employee employee) {
		this.employeeRepository.delete(employee);
	}

	public void deleteAll() {
		this.employeeRepository.deleteAll();
	}

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	@Override
	public void bulkInsert(List<Employee> employees, int batchSize) {
		DBCollection collection = db
				.getCollection(EmployeeProperties.COLLECTION);
		List<DBObject> docs = new ArrayList<DBObject>();
		EmployeeTransformer transformer = new EmployeeTransformer();
		WriteResult result = null;

		int insertCounter = 0;

		for (Employee employee : employees) {
			docs.add((DBObject) transformer.transform(employee));
			insertCounter++;

			if (insertCounter == batchSize) {
				result = collection.insert(docs);
				log.info(result.getError());
				insertCounter = 0;
				docs = new ArrayList<DBObject>();
			}
		}

		// catch last docs
		if (insertCounter > 0 && !docs.isEmpty()) {
			collection.insert(docs);
		}
	}
}
