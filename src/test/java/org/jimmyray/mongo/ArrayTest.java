package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.DepartmentProperties;
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
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Test Array query operations.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class ArrayTest {
	private static Logger log = LoggerFactory.getLogger(ArrayTest.class);

	private MongoOperations mongoOps;
	private List<Employee> employees;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");
		EmployeeShortLoader.main(null);
		employees = mongoOps.findAll(Employee.class);
	}

	@Test
	public void testSpringData() {
		Query query = new Query();
		query.addCriteria(Criteria
				.where(EmployeeProperties.DEPARTMENT + "."
						+ DepartmentProperties.NAME).is("Sales"));

		employees = this.mongoOps.find(query, Employee.class);

		assertNotNull("Employees list was null.", employees);
		log.info("Employees list count = " + employees.size());
		assertEquals("Employees list count was not correct.", 28,
				employees.size());

		List<String> products = new ArrayList<String>();
		products.add("Softball");
		products.add("Baseball");

		Update update = new Update();
		update.set("products", products);
		mongoOps.updateMulti(query, update, Employee.class);

		query = new Query();
		query.addCriteria(Criteria.where("products").all(products));
		employees = this.mongoOps.find(query, Employee.class);
		log.info(employees.size() + " all products.");

		assertNotNull("Employees list was null.", employees);
		assertEquals("Employees list count was not correct.", 28,
				employees.size());

		products.add("Football");
		query = new Query();
		query.addCriteria(Criteria.where("products").all(products));
		employees = this.mongoOps.find(query, Employee.class);

		assertNotNull("Employees list was null.", employees);
		assertEquals("Employees list count was not correct.", 0,
				employees.size());
		log.info(employees.size() + " added football.");

		query = new Query();
		query.addCriteria(Criteria.where("products").is("Baseball"));
		update = new Update();
		update.pull("products", "Baseball");
		mongoOps.updateMulti(query, update, Employee.class);

		query = new Query();
		query.addCriteria(Criteria.where("products").is("Baseball"));
		employees = this.mongoOps.find(query, Employee.class);

		log.info(employees.size() + " pulled baseball.");

		assertEquals("Employees list size was incorrect", 0, employees.size());

	}

	@Test
	public void testMongoDriver() {
		assertNotNull("employees was null.", employees);
		assertEquals("employees count was not correct.", 161, employees.size());

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		assertNotNull("employees was null.", collection);
		assertEquals("employees count was not correct.", 161,
				collection.count());

		DBObject query = BasicDBObjectBuilder
				.start()
				.add(EmployeeProperties.DEPARTMENT + "."
						+ DepartmentProperties.NAME, "Sales").get();

		log.info(query.toString());
		DBCursor cursor = collection.find(query);

		assertNotNull("cursor was null.", cursor);
		log.info("Cursor count = " + cursor.count());
		assertEquals("cursor count was not correct.", 28, cursor.count());

		List<String> products = new ArrayList<String>();
		products.add("Softball");
		products.add("Baseball");
		DBObject update = new BasicDBObject();
		update.put("$set", new BasicDBObject("products", products));
		collection.updateMulti(query, update);

		query = new BasicDBObject();
		query.put("products", new BasicDBObject("$all", products));

		cursor = collection.find(query);
		log.info(cursor.count() + " all products.");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 28, cursor.count());

		products.add("Football");
		query = new BasicDBObject();
		query.put("products", new BasicDBObject("$all", products));

		cursor = collection.find(query);
		log.info(cursor.count() + " added football.");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 0, cursor.count());

		query = new BasicDBObject("products", "Baseball");
		update = new BasicDBObject();
		update.put("$pull", new BasicDBObject("products", "Baseball"));

		collection.updateMulti(query, update);

		query.put("products", "Baseball");
		cursor = collection.find(query);

		log.info(cursor.size() + " pulled baseball.");

		assertEquals("Cursor size was incorrect", 0, cursor.size());

	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
