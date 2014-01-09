package org.jimmyray.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jimmyray.mongo.data.loaders.LocationLoader;
import org.jimmyray.mongo.data.model.Location;
import org.jimmyray.mongo.framework.GeoUtils;
import org.jimmyray.mongo.services.LocationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.Circle;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


//JVM Settings -Xmx512M -Dspring.profiles.active=location
public class LocationQueryTest {
	private static Logger log = LoggerFactory
			.getLogger(LocationQueryTest.class);

	private ApplicationContext ctx;
	//private static Mongo mongo;
	private static MongoOperations mongoOps;
	List<Location> locations;
	LocationService locationService;

	@Before
	public void setup() {
		ctx = new GenericXmlApplicationContext("context/main.xml");
		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
		//mongo = (Mongo) ctx.getBean("mongo");
		this.locationService = (LocationService) ctx.getBean("locationService");
		LocationLoader.main(null);
	}

	/**
	 * Test $geoWithin query of LocationRepository.
	 * 
	 * @see LocationService#findByGeoWithin(Circle)
	 */
	@Test
	public void testLocationCircleQuery() {
		log.debug("<<<<<<<<<<<<<<<<<  testLocationCircleQuery  >>>>>>>>>>>>>>>>>>>>");

		/*
		 * List<Location> locations = locationService.findByCityAndState(
		 * "Wheeling", "WV");
		 */

		List<Location> locations = locationService
				.findByCityAndStateAndZipCode("Wheeling", "WV", "26003");

		log.debug("List Size:  " + locations.size());

		assertNotNull("locations[0] was null.", locations.get(0));

		assertEquals("City was not correct.", "Wheeling", locations.get(0)
				.getCity());
		assertEquals("State was not correct.", "WV", locations.get(0)
				.getState());
		assertEquals("ZipCode was not correct.", "26003", locations.get(0)
				.getZipCode());

		// Used to troubleshoot Location Repo
		/*
		 * DBObject query = new BasicDBObject(); query.put("city", "Wheeling");
		 * query.put("state", "WV");
		 * 
		 * DBObject fields = new BasicDBObject(); fields.put("_id", 0);
		 * fields.put("city", 1); fields.put("state", 2);
		 * 
		 * DBCollection collection = this.mongoOps.getCollection("locations");
		 * DBCursor cursor = collection.find(query, fields);
		 * 
		 * log.info(cursor.size()+"");
		 * 
		 * log.info(cursor.toString());
		 * 
		 * while (cursor.hasNext()) { log.info(cursor.next().toString()); }
		 */

		List<Location> locales = this.locationService
				.findByGeoWithin(new Circle(locations.get(0).getLongitude(),
						locations.get(0).getLatitude(), 1));

		for (Location locale : locales) {
			log.info(locale.toString());
		}

		assertEquals("City was not correct.", "Aliquippa", locales.get(0)
				.getCity());
		assertEquals("City was not correct.", "Conway", locales.get(19)
				.getCity());

	}

	/**
	 * Test $near query of LocationRepository. Requires 2dsphere index.
	 * 
	 * @see LocationService#findNear(double, double, double)
	 */
	@Test
	public void testNear() {
		log.info("<<<<<<<<<<<<<<<<<  testNear  >>>>>>>>>>>>>>>>>>>>");
		//DB db = mongo.getDB(Properties.getString("mongodb.db.locs"));
		DBObject index = new BasicDBObject();
		index.put("coords", "2dsphere");
		mongoOps.getCollection("locations").ensureIndex(index);

		List<Location> locations = locationService.findByCityAndState(
				"Wheeling", "WV");

		assertNotNull("locations[0] was null.", locations.get(0));

		assertEquals("City was not correct.", "Wheeling", locations.get(0)
				.getCity());
		assertEquals("State was not correct.", "WV", locations.get(0)
				.getState());
		assertEquals("ZipCode was not correct.", "26003", locations.get(0)
				.getZipCode());

		List<Location> locales = this.locationService.findNear(locations.get(0)
				.getLongitude(), locations.get(0).getLatitude(), GeoUtils
				.milesToMeters(5));

		for (Location locale : locales) {
			log.info(locale.toString());
		}

		assertEquals("City was not correct.", "Yorkville", locales.get(2)
				.getCity());
		assertEquals("City was not correct.", "Glen Dale", locales.get(14)
				.getCity());
	}

	/**
	 * Test $near query of LocationRepository. Requires 2dsphere index. Uses
	 * overloaded convenience method in LocationService.
	 * 
	 * @see LocationService#findNear(Location, double)
	 */
	@Test
	public void testNearMiles() {
		log.info("<<<<<<<<<<<<<<<<<  testNearMiles  >>>>>>>>>>>>>>>>>>>>");

		//DB db = mongo.getDB(Properties.getString("mongodb.db.locs"));
		DBObject index = new BasicDBObject();
		index.put("coords", "2dsphere");
		mongoOps.getCollection("locations").ensureIndex(index);

		List<Location> locations = locationService.findByCityAndState(
				"Wheeling", "WV");

		assertNotNull("locations[0] was null.", locations.get(0));

		assertEquals("City was not correct.", "Wheeling", locations.get(0)
				.getCity());
		assertEquals("State was not correct.", "WV", locations.get(0)
				.getState());
		assertEquals("ZipCode was not correct.", "26003", locations.get(0)
				.getZipCode());

		List<Location> locales = this.locationService.findNear(
				locations.get(0), 5);

		for (Location locale : locales) {
			log.info(locale.toString());
		}

		assertEquals("City was not correct.", "Yorkville", locales.get(2)
				.getCity());
		assertEquals("City was not correct.", "Glen Dale", locales.get(14)
				.getCity());
	}

	@Test
	public void testNearPoint() {
		log.info("<<<<<<<<<<<<<<<<<  testNearPoint  >>>>>>>>>>>>>>>>>>>>");

		//DB db = mongo.getDB(Properties.getString("mongodb.db.locs"));
		DBObject index = new BasicDBObject();
		index.put("coords", "2dsphere");
		mongoOps.getCollection("locations").ensureIndex(index);

		List<Location> locations = locationService.findByCityAndState(
				"Wheeling", "WV");

		assertNotNull("locations[0] was null.", locations.get(0));

		assertEquals("City was not correct.", "Wheeling", locations.get(0)
				.getCity());
		assertEquals("State was not correct.", "WV", locations.get(0)
				.getState());
		assertEquals("ZipCode was not correct.", "26003", locations.get(0)
				.getZipCode());

		GeoResults<Location> results = locationService.findNearPoint(
				locations.get(0), 5);

		for (GeoResult<Location> result : results) {
			log.info(result.getContent().toString()
					+ " - Distance = "
					+ Math.round(10.0 * result.getDistance()
							.getNormalizedValue()) / 10.0 + " miles");
		}

		assertEquals("City was not correct.", "Yorkville", results.getContent()
				.get(2).getContent().getCity());
		assertEquals("City was not correct.", "Glen Dale", results.getContent()
				.get(14).getContent().getCity());
	}

	@After
	public void cleanDb() {
		log.debug("Cleaning...");
		mongoOps.dropCollection(Location.class);
		log.debug("DBName:  " + mongoOps.getCollection("locations").getDB().getName());
		log.debug("Locations Count:  " + mongoOps.getCollection("locations").count());
	}

}
