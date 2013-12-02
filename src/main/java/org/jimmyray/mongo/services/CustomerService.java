package org.jimmyray.mongo.services;

import java.util.List;

import org.jimmyray.mongo.data.model.Customer;

import com.mongodb.DBCollection;

/**
 * CustomerService interface defines employee service contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface CustomerService {
	Customer saveCustomer(Customer customer);

	void saveCustomers(List<Customer> customers);

	List<Customer> findAll();

	List<Customer> findByCustomerName(String customerName);

	Customer findByCustomerId(String customerId);

	void deleteCustomer(Customer customer);

	void deleteAll();

	List<String> getCollectionJson(final DBCollection collection);
}
