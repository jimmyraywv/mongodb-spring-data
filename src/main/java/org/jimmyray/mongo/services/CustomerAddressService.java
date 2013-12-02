package org.jimmyray.mongo.services;

import java.util.List;

import org.jimmyray.mongo.data.model.CustomerAddress;

/**
 * CustomerAddressService interface defines employee service contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface CustomerAddressService {
	CustomerAddress saveCustomerAddress(CustomerAddress customerAddress);

	void saveCustomerAddresses(List<CustomerAddress> customerAddresses);

	List<CustomerAddress> findAll();
	
	List<CustomerAddress> findByCity(String city);

	List<CustomerAddress> findByState(String state);
	
	List<CustomerAddress> findByCountry(String country);

	void deleteCustomerAddress(CustomerAddress customerAddress);

	void deleteAll();
}
