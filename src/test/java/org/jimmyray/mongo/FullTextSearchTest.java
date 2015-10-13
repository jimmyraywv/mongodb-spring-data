package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

public class FullTextSearchTest {

	private static Logger log = LoggerFactory
			.getLogger(FullTextSearchTest.class);

	private ApplicationContext ctx;
	private Mongo mongo;
	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		ctx = new GenericXmlApplicationContext(
				"context/main.xml");
		mongo = (Mongo) ctx.getBean("mongo");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");

		EmployeeShortLoader.main(null);
	}

	@Test
	public void testTextSearch() {
		DB db = mongo.getDB(Properties.getString("mongodb.db"));
		DBObject index = new BasicDBObject();
		index.put(EmployeeProperties.TITLE, "text");
		mongoOps.getCollection(EmployeeProperties.COLLECTION)
				.createIndex(index);

		/*
		 * if (!db.isAuthenticated()) { boolean auth = db.authenticate(
		 * Properties.getString("mongodb.user"),
		 * Properties.getString("mongodb.pwd").toCharArray());
		 * 
		 * assertTrue("User did not authenticate to MongoDB.", auth); }
		 */

		final DBObject command = new BasicDBObject();
		command.put("text", EmployeeProperties.COLLECTION);
		command.put("search", "SeNiOr");
		// command.put("limit", 2);
		final CommandResult result = db.command(command);

		assertNotNull("Result was null.", result);

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getJsonFactory();
			JsonParser parser = factory.createJsonParser(result.toString());
			JsonNode node = mapper.readTree(parser);

			assertEquals("Node size was incorrect." + result.toString(), 6,
					node.size());

			Iterator<JsonNode> looper = node.getElements();
			int counter = 0;
			while (looper.hasNext()) {
				node = looper.next();
				if (node.isArray()) {
					Iterator<JsonNode> nodes = node.getElements();
					JsonNode doc = null;
					while (nodes.hasNext()) {
						counter++;
						doc = nodes.next().get("obj");
						log.info(doc.get("lastName") + " - " +
						doc.get("title"));
					}
				}
			}

			assertEquals("Counter was not correct.", 67, counter);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@Test
	public void testTextWithMongoJSON() {
		DB db = mongo.getDB(Properties.getString("mongodb.db"));

		/*
		 * if (!db.isAuthenticated()) { boolean auth = db.authenticate(
		 * Properties.getString("mongodb.user"),
		 * Properties.getString("mongodb.pwd").toCharArray());
		 * 
		 * assertTrue("User did not authenticate to MongoDB.", auth); }
		 */

		final DBObject command = new BasicDBObject();
		command.put("text", EmployeeProperties.COLLECTION);
		command.put("search", "SeNiOr");

		DBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.ID, 0);
		fields.put(EmployeeProperties.LAST_NAME, 1);
		fields.put(EmployeeProperties.TITLE, 1);

		command.put("project", fields);
		command.put("limit", 1);
		final CommandResult result = db.command(command);

		assertNotNull("Result was null.", result);

		try {
			DBObject results = (DBObject) ((DBObject) JSON.parse(result
					.toString())).get("results");

			assertEquals("Counter was not correct.", 67, results.keySet()
					.size());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
