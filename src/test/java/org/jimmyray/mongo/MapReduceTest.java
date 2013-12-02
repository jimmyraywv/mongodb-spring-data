package org.jimmyray.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.js.MapReduce;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

public class MapReduceTest {

	private static Logger log = LoggerFactory.getLogger(MapReduceTest.class);

	private MongoOperations mongoOps;
	private ClassPathXmlApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext(
				"context/main-HQ.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");

		EmployeeShortLoader.main(null);
	}

	@Test
	public void testSumSalaryByTitle() {
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);
		long startTime = System.currentTimeMillis();
		MapReduceOutput out = collection.mapReduce(
				(String) MapReduce.MAP_FUNCTION,
				(String) MapReduce.REDUCE_FUNCTION, null,
				MapReduceCommand.OutputType.INLINE, null);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("MapReduce time: " + duration + "ms");

		for (DBObject obj : out.results()) {
			log.info(obj.toString());
		}

		log.info("\n\n\n");
	}

	@Test
	public void testSumSalaryByTitleSpringData() {

		long startTime = System.currentTimeMillis();

		MapReduceResults<Employee> employees = this.mongoOps.mapReduce(
				EmployeeProperties.COLLECTION, (String) MapReduce.MAP_FUNCTION,
				(String) MapReduce.REDUCE_FUNCTION, Employee.class);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("Spring Data MapReduce time: " + duration + "ms");

		BasicDBList list = (BasicDBList) employees.getRawResults().get(
				"results");

		for (Object object : list) {
			log.info(object.toString());
		}

		log.info("\n\n\n");
	}

	@Test
	public void testAverageFinalize() {
		DBCollection collection = mongoOps
				.getCollection(EmployeeProperties.COLLECTION);

		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

		DBObject query = new BasicDBObject();
		List<BasicDBObject> orParms = new ArrayList<BasicDBObject>();
		orParms.add(new BasicDBObject(EmployeeProperties.DEPARTMENT + ".name",
				"Sales"));
		orParms.add(new BasicDBObject(EmployeeProperties.DEPARTMENT + ".name",
				"Research"));

		query.put("$or", orParms);
		DBObject fields = new BasicDBObject();
		fields.put("_id", 0);
		fields.put("ttl", 1);
		fields.put("pay", 1);

		@SuppressWarnings("unchecked")
		List<String> titles = collection.distinct(EmployeeProperties.TITLE,
				query);

		for (String title : titles) {
			map.put(title, new ArrayList<Integer>());
		}

		DBCursor cursor = collection.find(query, fields);

		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			map.get(doc.get("ttl")).add((Integer) doc.get("pay"));
		}

		MapReduceCommand command = new MapReduceCommand(collection,
				MapReduce.MAP_FUNCTION.toString(),
				MapReduce.REDUCE_FUNCTION.toString(), null,
				MapReduceCommand.OutputType.INLINE, query);

		command.setFinalize(MapReduce.FINALIZE_FUNCTION.toString());

		long startTime = System.currentTimeMillis();
		MapReduceOutput output = collection.mapReduce(command);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("MapReduce time: " + duration + "ms");

		for (DBObject obj : output.results()) {
			String key = (String) obj.get("_id");
			log.debug("Line 140:  " + key);
			log.info(obj.toString());
			log.info("{salaries : " + map.get(key).toString() + "}\n");
		}

		log.info("\n\n\n");
	}

	@Test
	public void testAverageFinalizeSpringData() {
		long startTime = System.currentTimeMillis();

		Query query = new Query();
		query.addCriteria(Criteria
				.where(EmployeeProperties.DEPARTMENT + ".name")
				.exists(true)
				.orOperator(
						Criteria.where(EmployeeProperties.DEPARTMENT + ".name")
								.is("Sales"),
						Criteria.where(EmployeeProperties.DEPARTMENT + ".name")
								.is("Research")));

		MapReduceOptions options = new MapReduceOptions();
		options.finalizeFunction((String) MapReduce.FINALIZE_FUNCTION);
		options.outputTypeInline();

		MapReduceResults<Employee> employees = this.mongoOps.mapReduce(query,
				EmployeeProperties.COLLECTION, (String) MapReduce.MAP_FUNCTION,
				(String) MapReduce.REDUCE_FUNCTION, options, Employee.class);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("Spring Data MapReduce time: " + duration + "ms");

		log.info(employees.toString());

		BasicDBList list = (BasicDBList) employees.getRawResults().get(
				"results");

		for (Object object : list) {
			log.info(object.toString());
		}
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
		this.ctx.close();
	}
}
