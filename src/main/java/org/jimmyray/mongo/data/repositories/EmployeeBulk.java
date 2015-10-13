package org.jimmyray.mongo.data.repositories;

import java.util.List;

import org.jimmyray.mongo.data.model.Employee;

/**
 * The Interface EmployeeBulk is used to create a custom repository to handle
 * bulk inserts.
 */
public interface EmployeeBulk {

	/**
	 * Insert employee records in bulk batch size
	 * 
	 * @param employees
	 * @param batchSize
	 */
	void bulkInsert(List<Employee> employees, int batchSize);
}
