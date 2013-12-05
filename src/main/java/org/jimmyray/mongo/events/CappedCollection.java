package org.jimmyray.mongo.events;

import org.jimmyray.mongo.framework.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class CappedCollection {
	private static Logger log = LoggerFactory.getLogger(CappedCollection.class);

	private Mongo mongo;

	public static void main(String[] args) {
		CappedCollection cappedCollection = new CappedCollection();

		cappedCollection.createCappedCollection(
				new GenericXmlApplicationContext(Properties
						.getString("springMongoConfig.path.configFile")),
				"HRDB", "events");
	}

	public DBCollection createCappedCollection(ApplicationContext ctx,
			String dbName, String collectionName) {
		mongo = (Mongo) ctx.getBean(Properties
				.getString("springMongoConfig.bean.mongo"));

		DB db = mongo.getDB(dbName);
		DBCollection collection = null;

		if (db.collectionExists(collectionName)) {
			collection = db.getCollection(collectionName);
		} else {
			DBObject options = BasicDBObjectBuilder.start().add("capped", true)
					.add("size", 10000000l).get();
			collection = db.createCollection(collectionName, options);
		}

		return collection;
	}
}
