package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

//JVM Settings -Xmx512M -Dspring.profiles.active=geo
public class GeoQueriesTest {
	private static Logger log = LoggerFactory.getLogger(GeoQueriesTest.class);

	private ApplicationContext ctx;
	MongoOperations mongoOps;
	Mongo mongo;
	List<Employee> employees;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		mongo = (Mongo) ctx.getBean("mongo");
	}

	@Test
	public void testStateQuery() {
		DBCollection collection = mongoOps.getCollection("geos");
		collection.ensureIndex("stateIndex");

		BasicDBObject query = new BasicDBObject("state", "MN");
		BasicDBObject fields = new BasicDBObject();
		fields.put("city", 1);
		fields.put("state", 1);

		DBCursor cursor = collection.find(query,fields);
		assertNotNull("cursor was null.", cursor);
		assertEquals("cursor count was not 888.", 888, cursor.count());

		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.info(dbo.toString());
			assertEquals("Keyset size not equal to 3.", 3, dbo.keySet().size());
		}
	}

	@Test
	public void testCenterQuery() {
		DBCollection collection = mongoOps.getCollection("zips");
		collection.ensureIndex(new BasicDBObject("loc", "2d"), "locIndex");

		BasicDBList coords = new BasicDBList();
		coords.add(-92.823358);
		coords.add(44.86965);

		BasicDBList parms = new BasicDBList();
		parms.add(coords);
		parms.add(.3);

		BasicDBObject query = new BasicDBObject("loc", new BasicDBObject(
				"$geoWithin", new BasicDBObject("$center", parms)));

		DBCursor cursor = collection.find(query);
		
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			log.info(dbo.toString());
			assertEquals("Keyset size not equal to 5.", 5, dbo.keySet().size());
		}
	}
}
