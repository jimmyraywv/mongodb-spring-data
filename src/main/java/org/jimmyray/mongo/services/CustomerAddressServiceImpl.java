package org.jimmyray.mongo.services;

import java.util.List;

import org.jimmyray.mongo.data.model.CustomerAddress;
import org.jimmyray.mongo.data.repository.CustomerAddressRepository;

/**
 * Implementation of EmployeeService contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class CustomerAddressServiceImpl implements CustomerAddressService {
	private CustomerAddressRepository customerAddressRepository;

	@Override
	public CustomerAddress saveCustomerAddress(CustomerAddress customerAddress) {
		return this.customerAddressRepository.save(customerAddress);
	}

	@Override
	public void saveCustomerAddresses(List<CustomerAddress> customerAddresses) {
		for (CustomerAddress customerAddress : customerAddresses) {
			this.saveCustomerAddress(customerAddress);
		}

	}

	@Override
	public List<CustomerAddress> findAll() {
		return this.customerAddressRepository.findAll();
	}

	@Override
	public List<CustomerAddress> findByCity(String city) {
		return this.customerAddressRepository.findByCity(city);
	}

	@Override
	public List<CustomerAddress> findByCountry(String country) {
		return this.customerAddressRepository.findByCountry(country);
	}

	@Override
	public List<CustomerAddress> findByState(String state) {
		return this.customerAddressRepository.findByState(state);
	}

	@Override
	public void deleteCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddressRepository.delete(customerAddress);
	}

	@Override
	public void deleteAll() {
		this.customerAddressRepository.deleteAll();
	}

	public void setCustomerAddressRepository(
			CustomerAddressRepository customerAddressRepository) {
		this.customerAddressRepository = customerAddressRepository;
	}

}
