package org.jimmyray.mongo;

import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;

public class SaveInsertUpdateUpsertTest {

	private static Logger log = LoggerFactory.getLogger(SaveInsertUpdateUpsertTest.class);
	private ApplicationContext ctx;
	private static MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		ctx = new GenericXmlApplicationContext("context/main-HQ.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	@Test
	public void testSaveEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setFirstName("Jimmy");
		employee.setLastName("Ray");
		mongoOps.save(employee);
		
		log.info("Fired Save.");

		employee = new Employee();
		employee.setFirstName("Griffin");
		employee.setLastName("Ray");
		mongoOps.insert(employee);
		
		log.info("Fired Insert.");
		
		mongoOps.getCollection("emps").update(
			    new BasicDBObject("fname", "Griffin"), 
			    new BasicDBObject("$set", new BasicDBObject("mname", "Tyler")), true, false);
		
		log.info("Fired Update.");
		
		Query query = new Query();
		query.addCriteria(Criteria
				.where(EmployeeProperties.FIRST_NAME).is("Griffin"));
		
		Update update = new Update();
		
		update.set(EmployeeProperties.TITLE, "Son");
		
		mongoOps.upsert(query, update, Employee.class);
		
		log.info("Fired Upsert.");
	}
}
