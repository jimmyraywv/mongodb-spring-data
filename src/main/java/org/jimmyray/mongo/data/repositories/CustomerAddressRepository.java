package org.jimmyray.mongo.data.repositories;

import java.util.List;

import org.jimmyray.mongo.data.model.CustomerAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB Repository for Customer Address objects. Methods are not
 * concretely implemented anywhere at design time. Methods are implemented at
 * Spring Container startup.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface CustomerAddressRepository extends
		MongoRepository<CustomerAddress, String> {

	List<CustomerAddress> findAll();

	List<CustomerAddress> findByCity(String city);
	
	List<CustomerAddress> findByState(String state);
	
	List<CustomerAddress> findByCountry(String country);

	CustomerAddress save(CustomerAddress customerAddress);

	void delete(CustomerAddress customerAddress);

	void deleteAll();
}
