package org.jimmyray.mongo;

import static org.junit.Assert.assertNotNull;

import org.jimmyray.mongo.data.config.SpringMongoConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Test Spring config and container startup. There are three (3) ways to
 * configure the context and container.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class ConfigTest {
	private MongoOperations mongoOps;

	@Before
	public void setUp() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		/*
		 * ApplicationContext ctx = new GenericXmlApplicationContext(
		 * "context/main.xml");
		 */

		mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");

		/*
		 * SpringBeanFactory.initContext(); mongoOps =
		 * (MongoOperations)SpringBeanFactory.getBean("mongoTemplate");
		 */
	}

	@Test
	public void testMongoOps() throws Exception {
		assertNotNull("Mongo Ops is null.", this.mongoOps);
	}
}
