package org.jimmyray.mongo.data.repository;

import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Spring Data MongoDB Repository for Employee objects. Methods are not
 * concretely implemented anywhere at design time. Methods are implemented at
 * Spring Container startup.
 * 
 * @author jimmyray
 * @version 1.0
 */
// @RepositoryDefinition(domainClass = Employee.class, idClass = String.class)
public interface EmployeeRepository extends MongoRepository<Employee, String>,
		QueryDslPredicateExecutor<Employee> {

	List<Employee> findAll();

	List<Employee> findByLastName(String lastName);

	List<Employee> findByFirstName(String lastName);

	List<Employee> findByGender(String gender);

	List<Employee> findByLastNameOrderByFirstNameAsc(String lastName);

	Employee findByEmployeeId(String employeeId);

	Employee save(Employee employee);

	void delete(Employee employee);

	void deleteAll();

	List<Employee> findByLastNameAndDepartmentName(String lastName,
			String departmentName);

	@Query("{lname:?0, dept.name:?1}")
	List<Employee> queryByEmployeeLastNameAndDepartmentName(String lastName,
			String deptName);
}
