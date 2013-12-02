package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jimmyray.mongo.data.loaders.CustomerLoader;
import org.jimmyray.mongo.data.model.Customer;
import org.jimmyray.mongo.services.CustomerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.DBCollection;

public class CustomerServiceTest {
	private static Logger log = LoggerFactory
			.getLogger(CustomerServiceTest.class);

	private MongoOperations mongoOps;
	private CustomerService customerService;

	@Before
	public void setup() {
		ApplicationContext ctx = new GenericXmlApplicationContext(
				"context/main.xml");
		customerService = (CustomerService) ctx.getBean("customerService");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		CustomerLoader.main(null);
	}

	@Test
	public void testEmployeeService() throws Exception {
		List<Customer> customers = customerService.findAll();
		assertTrue("Customers list is not empty.", !customers.isEmpty());

		DBCollection collection = mongoOps.getCollection("customers");
		log.info(collection.getFullName());
		log.info(collection.count() + "");
		List<String> jsonList = customerService.getCollectionJson(collection);
		log.info(jsonList.toString());

		log.info(jsonList.toString());

		assertEquals("List count is incorrect", 123, jsonList.size());

	}

	@After
	public void tearDown() {
		DBCollection collection = mongoOps.getCollection("customers");
		collection.drop();
		collection = mongoOps.getCollection("customerAddresses");
		collection.drop();
	}

}
