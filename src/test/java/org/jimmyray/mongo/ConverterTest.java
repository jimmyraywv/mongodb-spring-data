package org.jimmyray.mongo;

import static org.junit.Assert.assertNotNull;

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
import org.springframework.data.mongodb.core.query.BasicQuery;

/**
 * Spring Data Converter Tests
 * 
 * @author jimmyray
 * @version 1.0
 */
// JVM Settings -Xmx512M -Dspring.profiles.active=convert
public class ConverterTest {
	private static Logger log = LoggerFactory.getLogger(ConverterTest.class);

	private static ApplicationContext ctx;
	private MongoOperations mongoOps;

	static {
		ctx = new GenericXmlApplicationContext(
				Properties.getString("springMongoConfig.path.configFile"));

		try {
			EmployeeShortLoader.main(null);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}

	@Before
	public void setup() {
		this.mongoOps = (MongoOperations) ctx.getBean(Properties
				.getString("springMongoConfig.bean.mongoTemplate"));
	}

	@Test
	public void testJsonConverter() {
		BasicQuery query = new BasicQuery("{" + EmployeeProperties.EMPLOYEE_ID
				+ ":'28241'}", "{'_id': 0 }");
		String result = this.mongoOps.findOne(query, String.class, "emps");

		assertNotNull("Result was null.", result);

		log.debug("Converted result:  " + result);
	}

	@After
	public void tearDown() {
		log.info("Cleaning...");
		this.mongoOps.dropCollection(Employee.class);
	}
}
