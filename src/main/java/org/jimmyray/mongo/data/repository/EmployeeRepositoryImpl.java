package org.jimmyray.mongo.data.repository;

import org.jimmyray.mongo.data.repository.custom.EmployeeBulkImpl;

/**
 * The Class EmployeeRepositoryImpl is needed for custom repository setup. It
 * links the impl of the EmployeeBulk with the Spring Data generated impl of the
 * EmployeeRepository.
 * 
 */
public class EmployeeRepositoryImpl extends EmployeeBulkImpl {

}
