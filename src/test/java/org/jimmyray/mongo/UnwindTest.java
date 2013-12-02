package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Map.Entry;

import org.jimmyray.mongo.data.loaders.DepartmentLoader;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UnwindTest {
	private static Logger log = LoggerFactory.getLogger(UnwindTest.class);

	private MongoOperations mongoOps;
	private Map<String, Map<String, Object>> deptMap;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");
		this.deptMap = DepartmentLoader.getDeptMap();

		for (Entry entry : deptMap.entrySet()) {
			mongoOps.insert(
					new BasicDBObject((Map<String, Object>) entry.getValue()),
					"depts");
		}
	}

	@Test
	public void testUnwind() {
		DBCollection collection = mongoOps.getCollection("depts");

		DBObject fields = new BasicDBObject("employees", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields);
		DBObject unwind = new BasicDBObject();
		unwind.put("$unwind", "$employees");
		DBObject match = new BasicDBObject();
		match.put("$match", new BasicDBObject("employees.lname", "Vural"));
		AggregationOutput output = collection.aggregate(project, unwind, match);

		int count = 0;
		for (Object object : output.results()) {
			count++;
		}

		assertEquals("Count was incorrect", 175, count);
	}
	
	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection("depts");
	}

}
