package org.jimmyray.mongo.data.loaders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jimmyray.mongo.data.model.Location;
import org.jimmyray.mongo.framework.ApplicationConstants;
import org.jimmyray.mongo.framework.Filer;
import org.jimmyray.mongo.framework.Properties;
import org.jimmyray.mongo.framework.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for creating Location objects.
 */
public final class LocationFactory {

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(LocationFactory.class);

	/** The Constant COMMON_DELIMITER. */
	private static final String COMMON_DELIMITER = Properties
			.getString("employeeFactory.delimiter.comma");

	/**
	 * Instantiates a new location factory.
	 */
	private LocationFactory() {

	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		loadData();
	}

	/**
	 * Load data.
	 * 
	 * @return the list
	 */
	public static List<Location> loadData() {
		List<Location> locations = new ArrayList<Location>();

		try {
			Filer filer = new Filer();

			List<String> lines = filer.readFileAsResource(Properties
					.getString("locationFactory.path.locationData")); //$NON-NLS-1$

			if (lines.size() == 0) {
				log.debug("No Locations Found");
			}

			for (String line : lines) {
				Location location = new Location();

				Scanner scanner = new Scanner(line);
				scanner.useDelimiter(LocationFactory.COMMON_DELIMITER);

				location.setZipCode(scanner.next());
				location.setCity(scanner.next());
				location.setState(scanner.next());
				location.setCoords(new double[] {
						Double.parseDouble(scanner.next()),
						Double.parseDouble(scanner.next()) });

				location.setTimeZone(Integer.parseInt(scanner.next()));
				location.setDstObserved(Strings.parseBoolean(scanner.next()));

				locations.add(location);
				//log.debug(location.toString());
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

		return locations;
	}

	/**
	 * Gets the map.
	 * 
	 * @param location
	 *            the location
	 * @return the map
	 */
	public static Map<String, Object> getMap(Location location) {
		Map<String, Object> locationMap = new LinkedHashMap<String, Object>();

		locationMap.put("_class", location.getClass().getName());
		if (location.getId() != null
				&& !location.getId().equals(ApplicationConstants.EMPTY_STRING)) {
			locationMap.put("id", location.getId());
		}

		locationMap.put("city", location.getCity());
		locationMap.put("state", location.getState());
		locationMap.put("coords", location.getCoords());
		locationMap.put("timeZone", location.getTimeZone());
		locationMap.put("zipCode", location.getZipCode());
		locationMap.put("dstObserved", location.isDstObserved());

		return locationMap;
	}
}
