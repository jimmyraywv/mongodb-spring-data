package org.jimmyray.mongo.data.config;

import java.net.UnknownHostException;

import org.jimmyray.mongo.framework.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Spring MongoDB configuration class.
 * 
 */
// @Configuration
// @Profile("main")
// -Dspring.profiles.active=main
public class SpringMongoConfig extends AbstractMongoConfiguration {
	private String databaseName = Properties.getString("mongodb.db"); //$NON-NLS-1$

	@Override
	@Bean
	public Mongo mongo() throws UnknownHostException {
		return new MongoClient(new MongoClientURI(
				Properties.getString("mongodb.url")));
	}

	@Override
	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		return new MongoTemplate(mongo(), this.databaseName);
	}

	@Override
	public String getDatabaseName() {
		return this.databaseName;
	}
}
