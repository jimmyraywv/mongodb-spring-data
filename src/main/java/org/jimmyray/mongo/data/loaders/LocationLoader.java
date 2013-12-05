package org.jimmyray.mongo.data.loaders;

import java.util.List;

import org.jimmyray.mongo.data.model.Location;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Location for Loader objects
 * 
 * @author jimmyray
 * 
 */
public final class LocationLoader {
	private static Logger log = LoggerFactory.getLogger(LocationLoader.class);
	
	private static final int BATCH_SIZE = 1000;

	private LocationLoader() {

	}

	public static void main(String[] args) {
		ApplicationContext ctx = new GenericXmlApplicationContext(
				"context/main.xml");

		LocationService locationService = (LocationService) ctx
				.getBean("locationService");

		long startTime = System.currentTimeMillis();
		List<Location> locations = LocationFactory.loadData();
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("Data load time: " + duration + "ms");
		log.info(Properties.getString("locationLoader.msg.saving")); //$NON-NLS-1$

		startTime = System.currentTimeMillis();
		locationService.bulkInsert(locations, LocationLoader.BATCH_SIZE);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		log.info("Data save time: " + duration + "ms");

		log.info(Properties.getString("locationLoader.mongoSave.msg.complete")); //$NON-NLS-1$
	}
}
