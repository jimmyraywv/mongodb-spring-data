package org.jimmyray.mongo;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.jimmyray.mongo.data.config.SpringMongoConfig;
import org.jimmyray.mongo.data.loaders.EmployeeLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.DepartmentProperties;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;

import com.mongodb.DBObject;

/**
 * Tests Spring Data API for Group functions, using a MapReduce example.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class EmployeeDeptGroupTest {
	private static Logger log = LoggerFactory
			.getLogger(EmployeeDeptGroupTest.class);

	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");

		EmployeeLoader.loadEmployees(ctx, true, 50000);
	}

	@Test
	public void testGroup() throws Exception {
		// Run Group Operation
		GroupByResults<Object> results = mongoOps
				.group(EmployeeProperties.COLLECTION,
						GroupBy.key(
								EmployeeProperties.DEPARTMENT + "."
										+ DepartmentProperties.NAME)
								.initialDocument("{ count: 0 }")
								.reduceFunction(
										"function(doc, prev) { prev.count ++ }"),
						Object.class);

		// Get Raw Results
		DBObject rawResults = results.getRawResults();
		log.info(rawResults.toString());

		// Parse raw results JSON
		ObjectMapper mapper = new ObjectMapper();
		Map<Object, Object> data = mapper.readValue(rawResults.toString(),
				Map.class);

		List<Object> retval = (List<Object>) data.get("retval");
		float count = 0;
		for (Object object : retval) {
			String currentDept = (((Map) object)
					.get(EmployeeProperties.DEPARTMENT + "."
							+ DepartmentProperties.NAME)).toString();
			String currentCount = (((Map) object).get("count")).toString();
			log.info(currentDept + "=" + currentCount);

			count += Float.parseFloat(currentCount);
		}

		log.info("Total=" + (int) count);
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
