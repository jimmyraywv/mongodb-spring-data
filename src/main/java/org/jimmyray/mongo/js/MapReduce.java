package org.jimmyray.mongo.js;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jimmyray.mongo.framework.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container class for JS.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class MapReduce {
	private static Logger log = LoggerFactory.getLogger(MapReduce.class);

	public static String MAP_FUNCTION;
	public static String REDUCE_FUNCTION;
	public static String FINALIZE_FUNCTION;

	static {
		MapReduce.init();
	}

	public static void main(String[] args) {
		log.debug(MapReduce.MAP_FUNCTION);
		log.debug(MapReduce.REDUCE_FUNCTION);
		log.debug(MapReduce.FINALIZE_FUNCTION);
		log.debug("Worked");
	}

	public static void init() {
		MapReduce.loadMapFunction();
		MapReduce.loadReduceFunction();
		MapReduce.loadFinalizeFunction();
	}

	private static void loadMapFunction() {
		try {
			InputStream is = MapReduce.class.getResourceAsStream(Properties
					.getString("mapReduce.path.mapFunction"));

			MapReduce.MAP_FUNCTION = IOUtils.toString(is);

		} catch (IOException ioe) {
			log.error(ioe.getLocalizedMessage());
		}
	}

	private static void loadReduceFunction() {
		try {
			InputStream is = MapReduce.class.getResourceAsStream(Properties
					.getString("mapReduce.path.reduceFunction"));

			MapReduce.REDUCE_FUNCTION = IOUtils.toString(is);

		} catch (IOException ioe) {
			log.error(ioe.getLocalizedMessage());
		}
	}

	private static void loadFinalizeFunction() {
		try {
			InputStream is = MapReduce.class.getResourceAsStream(Properties
					.getString("mapReduce.path.finalizeFunction"));

			MapReduce.FINALIZE_FUNCTION = IOUtils.toString(is);

		} catch (IOException ioe) {
			log.error(ioe.getLocalizedMessage());
		}
	}
}
