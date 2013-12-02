package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.regex.Pattern;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
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
import com.mongodb.QueryBuilder;

public class RegExQueryTest {
	private static Logger log = LoggerFactory.getLogger(RegExQueryTest.class);

	private ApplicationContext ctx;
	MongoOperations mongoOps;
	List<Employee> employees;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		try {
			EmployeeShortLoader.main(null);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@Test
	public void testRegExFind() {
		Pattern pattern = Pattern.compile("seNior EngIneer",
				Pattern.CASE_INSENSITIVE);
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.TITLE, pattern);

		BasicDBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.LAST_NAME, 1);
		fields.put(EmployeeProperties.TITLE, 1);

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		DBCursor cursor = collection.find(query, fields).sort(
				new BasicDBObject(EmployeeProperties.LAST_NAME, -1));

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not 53.", 53, cursor.count());

		log.info("RegEx Query (" + query.toString() + ")");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			// log.info(dbo.toString());
			assertEquals("Keyset size not equal to 2.", 2, dbo.keySet().size());
		}
	}

	@Test
	public void testRegExQueryBuilder() {
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		Pattern patternTitle = Pattern.compile("^senior",
				Pattern.CASE_INSENSITIVE);
		Pattern patternLastName = Pattern.compile("^Baik$",
				Pattern.CASE_INSENSITIVE);
		DBObject query = QueryBuilder
				.start()
				.or(QueryBuilder.start(EmployeeProperties.TITLE)
						.regex(patternTitle).get(),
						QueryBuilder.start(EmployeeProperties.LAST_NAME)
								.regex(patternLastName).get()).get();

		BasicDBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.LAST_NAME, 1);
		fields.put(EmployeeProperties.TITLE, 1);

		DBCursor cursor = collection.find(query, fields).sort(
				new BasicDBObject(EmployeeProperties.LAST_NAME, -1));

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count incorrect.", 67, cursor.count());

		log.info("RegEx Query (" + query.toString() + ")");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			// log.info(dbo.toString());
			assertEquals("Keyset size was incorrect.", 2, dbo.keySet().size());
		}
	}

	@Test
	public void testRegExFindSpringData() {
		Pattern pattern = Pattern.compile("seNior EngIneer",
				Pattern.CASE_INSENSITIVE);

		Query query = new Query();
		query.addCriteria(Criteria.where(EmployeeProperties.TITLE).is(pattern));
		query.fields().exclude(EmployeeProperties.ID)
				.include(EmployeeProperties.LAST_NAME)
				.include(EmployeeProperties.TITLE);

		List<Employee> employees = this.mongoOps.find(query, Employee.class);

		log.info(employees.size() + "");

		assertNotNull("List was null.", employees);
		assertEquals("List count was incorrect.", 53, employees.size());

		log.info("RegEx Query (" + query.toString() + ")");

		for (Employee employee : employees) {
			assertNotNull("Last name was null.", employee.getLastName());
			assertNotNull("Title was null.", employee.getTitle());
		}
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
