package org.jimmyray.mongo.data.config;

import java.net.UnknownHostException;

import org.jimmyray.mongo.framework.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;

/**
 * Spring MongoDB configuration class.
 * 
 */
// @Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
	private String databaseName = Properties
			.getString("mongodb.db"); //$NON-NLS-1$

	@Override
	@Bean
	public Mongo mongo() throws UnknownHostException {
		return new Mongo(
				Properties.getString("mongodb.url")); //$NON-NLS-1$
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
