package org.jimmyray.mongo.services;

import java.util.List;

import org.jimmyray.mongo.data.model.Location;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;

/**
 * LocationService interface defines location service contract.
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface LocationService {
	Location saveLocation(Location location);

	void init();

	void saveLocations(List<Location> locations);

	List<Location> findByCityAndState(String city, String state);

	List<Location> findByCityAndStateAndZipCode(String city, String state,
			String zipCode);

	List<Location> listLocations();

	Location getLocationById(String id);

	List<Location> findAll();

	List<Location> findByCity(String city);

	List<Location> findByState(String state);

	List<Location> findByZipCode(String zipCode);

	void bulkInsert(List<Location> locations, int batchSize);

	void deleteLocation(Location location);

	void deleteAll();

	List<Location> findByGeoWithin(Circle circle);
	
	List<Location> findNear(double lon, double lat, double distance);
	
	List<Location> findNear(Location location, double distanceInMiles);
	
	GeoResults<Location> findNearPoint(Location location, double distanceInMiles);
}
