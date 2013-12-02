package org.jimmyray.mongo.data.repository;

import java.util.List;

import org.jimmyray.mongo.data.model.Location;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * Spring Data MongoDB Repository for Location objects. Methods are not
 * concretely implemented anywhere at design time. Methods are implemented at
 * Spring Container startup.
 * 
 * @author jimmyray
 * @version 1.0
 */
@RepositoryDefinition(domainClass = Location.class, idClass = String.class)
public interface LocationRepository {
	
	@Query("{city:?0,state:?1}")  //Does not work correctly without annotation
	List<Location> findByCityAndState(String city, String state);

	List<Location> findByCityAndStateAndZipCode(String city, String state, String zipCode);

	List<Location> findAll();

	List<Location> findByCity(String city);

	List<Location> findByState(String state);

	List<Location> findByZipCode(String zipCode);

	Location findById(String id);

	Location save(Location location);

	void delete(Location location);

	void deleteAll();

	// GEO Spatials
	@Query("{ 'coords' : { $geoWithin : { $center : [ [ ?0, ?1 ] , ?2 ] } } }")
	List<Location> findByGeoWithin(double lon, double lat, double radius);
	
	@Query("{ 'coords' : { $near : { $geometry : { type : 'Point' ,coordinates : [ ?0 , ?1 ] } }, $maxDistance : ?2} }")
	List<Location> findByGeoNear(double lon, double lat, double distance);
}
