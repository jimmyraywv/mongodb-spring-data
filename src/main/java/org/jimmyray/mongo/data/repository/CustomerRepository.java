package org.jimmyray.mongo.data.repository;

import java.util.List;

import org.jimmyray.mongo.data.model.Customer;
import org.jimmyray.mongo.data.repository.custom.RepositoryUtils;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB Repository for Customer objects. Methods are not
 * concretely implemented anywhere at design time. Methods are implemented at
 * Spring Container startup.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface CustomerRepository extends MongoRepository<Customer, String>,
		RepositoryUtils {

	List<Customer> findAll();

	List<Customer> findByCustomerName(String customerName);

	Customer findByCustomerId(String customerId);

	Customer save(Customer customer);

	void delete(Customer customer);

	void deleteAll();
}
