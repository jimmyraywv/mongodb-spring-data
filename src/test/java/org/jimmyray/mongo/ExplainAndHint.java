package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class ExplainAndHint {
	private static Logger log = LoggerFactory.getLogger(ExplainAndHint.class);

	private ApplicationContext ctx;
	MongoOperations mongoOps;
	List<Employee> employees;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");

		EmployeeLoader.loadEmployees(ctx, true, 50000);
	}

	@Test
	public void testBasicQuery() {
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.EMPLOYEE_ID, "28241");

		BasicDBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.LAST_NAME, 1);
		fields.put(EmployeeProperties.EMPLOYEE_ID, 1);

		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		log.info("MongoDB Query Explain Plan:  "
				+ collection
						.find(query, fields)
						.hint(new BasicDBObject(EmployeeProperties.EMPLOYEE_ID,
								1)).explain());
		DBCursor cursor = collection.find(query, fields).hint(
				new BasicDBObject(EmployeeProperties.EMPLOYEE_ID, 1));

		log.info(cursor.count() + "");

		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not 1.", 1, cursor.count());

		log.info("Query (" + query.toString() + ")");

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.info(dbo.toString());
			assertEquals("Keyset size not equal to 2.", 2, dbo.keySet().size());
		}

		log.info(cursor.explain().toString());
	}

	@Test
	public void testBasicQuerySpringData() {
		Query query = new Query();
		query.addCriteria(
				Criteria.where(EmployeeProperties.EMPLOYEE_ID).is("28241")).withHint(EmployeeProperties.EMPLOYEE_ID)
				.fields().exclude(EmployeeProperties.ID)
				.include(EmployeeProperties.LAST_NAME)
				.include(EmployeeProperties.EMPLOYEE_ID);
		
		List<Employee> employees = this.mongoOps.find(query, Employee.class);
		log.info(employees.toString());

		log.info(employees.size() + "");

		assertNotNull("List was null.", employees);
		assertEquals("List count was incorrect.", 1, employees.size());

		log.info("Query (" + query.toString() + ")");
	}

	@After
	public void cleanDb() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
