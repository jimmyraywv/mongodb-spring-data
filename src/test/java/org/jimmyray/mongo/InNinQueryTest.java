package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class InNinQueryTest {
	private static Logger log = LoggerFactory.getLogger(InNinQueryTest.class);

	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");
		EmployeeLoader.loadEmployees(true, 50000);
	}

	@Test
	public void testIn() {
		List<String> names = new ArrayList<String>();
		names.add("Baby");
		names.add("Chenney");

		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.LAST_NAME, new BasicDBObject("$in", names));

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		DBCursor cursor = collection.find(query);

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not 406.", 406, cursor.count());

		query = new BasicDBObject();
		query.put(EmployeeProperties.LAST_NAME,
				new BasicDBObject("$nin", names));
		cursor = collection.find(query);

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 299618, cursor.count());
	}
	
	@Test
	public void testInSpringData() {
		List<String> names = new ArrayList<String>();
		names.add("Baby");
		names.add("Chenney");
		
		Query query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.LAST_NAME).in(names));
		
		List<Employee> list = this.mongoOps.find(query, Employee.class);

		assertNotNull("List was null.", list);
		assertEquals("List count was incorrect", 406, list.size());
		
		query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.LAST_NAME).nin(names));

		list = this.mongoOps.find(query, Employee.class);
		
		assertNotNull("List was null.", list);
		assertEquals("List count was incorrect", 299618, list.size());
	}

	@After
	public void cleanDb() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
