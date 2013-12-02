package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jimmyray.mongo.data.loaders.EmployeeLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BasicQueryTest {
	private static Logger log = LoggerFactory.getLogger(BasicQueryTest.class);

	private static boolean cleanFlag = false;

	private ApplicationContext ctx;
	private MongoOperations mongoOps;

	static {
		EmployeeLoader.loadEmployees(true, 10000);
	}

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main-auth.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	@Test
	public void testFindAll() {
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		int count = collection.find().count();
		log.info(count + ""); // runs on server
		assertEquals("Count was incorrect.", 300024, count);

		DBCursor cursor = collection.find().limit(1000); // Keep limits
		assertEquals("Cursor count was incorrect.", 300024, cursor.count());

		count = 0;

		while (cursor.hasNext()) {
			cursor.next();
			count++;
		}

		assertEquals("Count was incorrect.", 1000, count);
	}

	@Test
	public void testBasicQuery() {
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.TITLE, "Senior Engineer");

		BasicDBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.LAST_NAME, 1);

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		DBCursor cursor = collection.find(query, fields);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not 97748.", 97748, cursor.count());

		log.info("Basic Query (ttl field = Senior Engineer)");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.debug(dbo.toString());
			assertEquals("Keyset size incorrect.", 1, dbo.keySet().size());
		}
	}

	@Test
	public void testBasicQueryWithCriteria() {
		Query query = new Query(Criteria.where(EmployeeProperties.TITLE).is(
				"Senior Engineer"));
		query.fields().exclude(EmployeeProperties.ID);
		query.fields().include(EmployeeProperties.LAST_NAME);
		query.fields().include(EmployeeProperties.TITLE);

		List<Employee> employees = mongoOps.find(query, Employee.class);
		assertNotNull("Employees list was null.", employees);
		assertTrue("Employees is empty.", !employees.isEmpty());
		assertEquals("Employee count was incorrect.", 97748, employees.size());

		if (log.isDebugEnabled()) {
			for (Employee emp : employees) {
				log.debug(emp.toString());
			}
		}
	}

	@Test
	public void testOrQuery() {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		List<BasicDBObject> orArgs = new ArrayList<BasicDBObject>();
		orArgs.add(new BasicDBObject(EmployeeProperties.LAST_NAME, "Baik"));
		orArgs.add(new BasicDBObject(EmployeeProperties.LAST_NAME, "Bashian"));

		query.put("$or", orArgs);

		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.TITLE, 1);
		fields.put(EmployeeProperties.LAST_NAME, 1);

		DBCursor cursor = collection.find(query, fields);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 343, cursor.count());

		log.info("Or Query (lname field = Baik or lname field = Bashian)");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.debug(dbo.toString());
			assertEquals("Keyset size not equal to 2.", 2, dbo.keySet().size());
		}
	}

	@Test
	public void testAndQuery() {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		List<BasicDBObject> andArgs = new ArrayList<BasicDBObject>();
		andArgs.add(new BasicDBObject(EmployeeProperties.TITLE,
				"Senior Engineer"));
		andArgs.add(new BasicDBObject(EmployeeProperties.LAST_NAME, "Bashian"));

		query.put("$and", andArgs);

		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.TITLE, 1);
		fields.put(EmployeeProperties.LAST_NAME, 1);

		DBCursor cursor = collection.find(query, fields);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 62, cursor.count());

		log.info("And Query (ttl field = Senior Engineer and lname field = Bashian)");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.debug(dbo.toString());
			assertEquals("Keyset size not equal to 2.", 2, dbo.keySet().size());
		}

	}

	@Test
	public void testAndQueryPuts() {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		query.put(EmployeeProperties.TITLE, "Senior Engineer");
		query.put(EmployeeProperties.LAST_NAME, "Bashian");

		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.TITLE, 1);
		fields.put(EmployeeProperties.LAST_NAME, 1);

		DBCursor cursor = collection.find(query, fields);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 62, cursor.count());

		log.info("And Query (ttl field = Senior Engineer and lname field = Bashian)");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.debug(dbo.toString());
			assertEquals("Keyset size not equal to 2.", 2, dbo.keySet().size());
		}
	}

	@Test
	public void testAndOrQuery() {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		List<BasicDBObject> andArgs = new ArrayList<BasicDBObject>();
		andArgs.add(new BasicDBObject(EmployeeProperties.TITLE, "Staff"));
		andArgs.add(new BasicDBObject(EmployeeProperties.LAST_NAME, "Debuse"));

		List<BasicDBObject> orArgs = new ArrayList<BasicDBObject>();
		orArgs.add(new BasicDBObject(EmployeeProperties.LAST_NAME, "Baik"));
		orArgs.add(new BasicDBObject(EmployeeProperties.LAST_NAME, "Bashian"));
		orArgs.add(new BasicDBObject("$and", andArgs));

		query.put("$or", orArgs);

		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.TITLE, 1);
		fields.put(EmployeeProperties.LAST_NAME, 1);

		DBCursor cursor = collection.find(query, fields);

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not correct.", 411, cursor.count());

		log.info("Or/And Query (lastName field = Baik or lastName field = Bashian or (title field = Staff and lastName field = Debuse))");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.debug(dbo.toString());
			assertEquals("Keyset size not equal to 2.", 2, dbo.keySet().size());
		}

		cleanFlag = true;
	}

	@After
	public void tearDown() {
		if (cleanFlag) {
			log.info("Cleaning...");
			mongoOps.dropCollection(Employee.class);
		}
	}
}
