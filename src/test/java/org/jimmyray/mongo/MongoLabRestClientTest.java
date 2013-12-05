package org.jimmyray.mongo;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.rest.client.MongoLabClient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

//JVM Settings -Xmx512M -Dspring.profiles.active=lab
public class MongoLabRestClientTest {
	private static Logger log = LoggerFactory
			.getLogger(MongoLabRestClientTest.class);

	private static ApplicationContext ctx;
	private MongoLabClient restClient;

	static {
		ctx = new GenericXmlApplicationContext(
				Properties.getString("springMongoConfig.path.configFile"));
	}

	@Before
	public void setup() {
		restClient = (MongoLabClient) ctx.getBean("mongoLabClient");
	}

	@Test
	public void testGetDbs() {
		String out = this.restClient.listDatabases();
		log.debug(out);
		assertTrue("The string \"free\" was not found.", out.contains("free"));
		assertEquals("The response lenght was incorrect.", 10, out.length());
	}
}
