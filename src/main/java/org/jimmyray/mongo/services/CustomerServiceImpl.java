package org.jimmyray.mongo.services;

import java.util.List;

import org.jimmyray.mongo.data.model.Customer;
import org.jimmyray.mongo.data.repository.CustomerRepository;

import com.mongodb.DBCollection;

/**
 * Implementation of CustomerService contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class CustomerServiceImpl implements CustomerService {
	private CustomerRepository customerRepository;

	@Override
	public Customer saveCustomer(Customer customer) {
		return this.customerRepository.save(customer);
	}

	@Override
	public void saveCustomers(List<Customer> customers) {
		for (Customer customer : customers) {
			this.saveCustomer(customer);
		}
	}

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public List<Customer> findByCustomerName(String customerName) {
		return customerRepository.findByCustomerName(customerName);
	}

	@Override
	public Customer findByCustomerId(String customerId) {
		return customerRepository.findByCustomerId(customerId);
	}

	@Override
	public void deleteCustomer(Customer customer) {
		customerRepository.delete(customer);
	}

	@Override
	public void deleteAll() {
		customerRepository.deleteAll();
	}

	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public List<String> getCollectionJson(DBCollection collection) {
		return this.customerRepository.getCollectionJson(collection);
	}
}
