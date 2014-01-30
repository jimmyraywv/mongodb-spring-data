package org.jimmyray.mongo;

import static org.junit.Assert.assertNotNull;

import org.jimmyray.mongo.data.loaders.EmployeeShortLoader;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Collection Callback Tests
 * 
 * @author jimmyray
 * @version 1.0
 */
// JVM Settings -Xmx512M -Dspring.profiles.active=local
public class CollectionCallbackTest {
	private static Logger log = LoggerFactory
			.getLogger(CollectionCallbackTest.class);

	private MongoOperations mongoOps;

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
		mongoOps = (MongoOperations) SpringBeanFactory.getBean("mongoTemplate");

		EmployeeShortLoader.main(null);
	}

	@Test
	public void testCollectionCallback() {
		final DBObject query = new BasicDBObject();
		query.put(EmployeeProperties.LAST_NAME, "Shumilov");
		query.put(EmployeeProperties.FIRST_NAME, "Jayson");
		final DBObject fields = new BasicDBObject();
		fields.put(EmployeeProperties.FIRST_NAME, 1);
		fields.put(EmployeeProperties.LAST_NAME, 1);
		fields.put(EmployeeProperties.ID, 0);

		String result = this.mongoOps.execute(EmployeeProperties.COLLECTION,
				new CollectionCallback<String>() {
					public String doInCollection(DBCollection collection) {
						DBCursor cursor = collection.find(query, fields);

						if (cursor.hasNext()) {
							Object object = cursor.next();
							return null == object ? null : object.toString();
						}

						return null;
					};
				});

		assertNotNull("Callback result is null.", result);
		log.debug(result);
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		mongoOps.dropCollection(Employee.class);
	}
}
