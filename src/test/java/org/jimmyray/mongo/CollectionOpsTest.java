package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;

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

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

public class CollectionOpsTest {
	private static Logger log = LoggerFactory
			.getLogger(CollectionOpsTest.class);

	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		ApplicationContext ctx = new GenericXmlApplicationContext(
				"context/main-HQ.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		EmployeeLoader.loadEmployees(ctx, true, 50000);
	}

	@Test
	public void testCollections() {
		Employee employee = new Employee();
		employee.setFirstName("Jimmy");
		employee.setLastName("Ray");
		mongoOps.save(employee);

		DBCollection collection = mongoOps.getCollection(EmployeeProperties.COLLECTION);
		assertEquals("Employees collection count was wrong.", 300025,
				collection.count());

		BasicDBObject dbo = new BasicDBObject();
		dbo.append(EmployeeProperties.LAST_NAME, "Ray");
		dbo.append(EmployeeProperties.FIRST_NAME, "Griffin");
		dbo.append(EmployeeProperties.CLASS, Employee.class.getName());
		collection.insert(dbo);

		collection = mongoOps.getCollection(EmployeeProperties.COLLECTION);
		assertEquals("Employees collection count was wrong.", 300026,
				collection.count());
	}

	@After
	public void tearDown() {
		BasicDBObject query = new BasicDBObject();
		query.put(EmployeeProperties.LAST_NAME, "Ray");

		DBCollection collection = mongoOps.getCollection(EmployeeProperties.COLLECTION);
		WriteResult result = collection.remove(query);
		log.info(result.toString());
	}
}
