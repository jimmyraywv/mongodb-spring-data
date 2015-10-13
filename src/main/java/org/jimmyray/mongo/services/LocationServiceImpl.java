package org.jimmyray.mongo.services;

import java.util.ArrayList;
import java.util.List;

import org.jimmyray.mongo.data.model.Location;
import org.jimmyray.mongo.data.repositories.LocationRepository;
import org.jimmyray.mongo.data.transformers.LocationTransformer;
import org.jimmyray.mongo.framework.GeoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.NearQuery;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * The Class LocationServiceImpl.
 */
public class LocationServiceImpl implements LocationService {
	private static Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

	/** The location repository. */
	private LocationRepository locationRepository;

	/** The mongo. */
	private Mongo mongo;

	/** The mongo. */
	private MongoOperations mongoOps;

	/** The db. */
	private DB db;

	/** dbName */
	private String dbName;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jimmyray.mongo.services.LocationService#init()
	 */
	@Override
	public void init() {
		log.debug(dbName);
		if (mongo != null) {
			db = mongo.getDB(this.dbName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#saveLocation(org.jimmyray
	 * .mongo.data .model.Location)
	 */
	@Override
	public Location saveLocation(Location location) {
		return this.locationRepository.save(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#saveLocations(java.util.List)
	 */
	@Override
	public void saveLocations(List<Location> locations) {
		for (Location location : locations) {
			this.saveLocation(location);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jimmyray.mongo.services.LocationService#listLocations()
	 */
	@Override
	public List<Location> listLocations() {
		return this.locationRepository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#getLocationById(java.lang
	 * .String)
	 */
	@Override
	public Location getLocationById(String id) {
		return this.locationRepository.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jimmyray.mongo.services.LocationService#findAll()
	 */
	@Override
	public List<Location> findAll() {
		return this.locationRepository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#findByCity(java.lang.String)
	 */
	@Override
	public List<Location> findByCity(String city) {
		return this.locationRepository.findByCity(city);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#findByState(java.lang.String)
	 */
	@Override
	public List<Location> findByState(String state) {
		return this.locationRepository.findByState(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jimmyray.mongo.services.LocationService#findByZipCode(java.lang.
	 * String )
	 */
	@Override
	public List<Location> findByZipCode(String zipCode) {
		return this.locationRepository.findByZipCode(zipCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#bulkInsert(java.util.List,
	 * int)
	 */
	@Override
	public void bulkInsert(List<Location> locations, int batchSize) {
		DBCollection collection = db.getCollection("locations");
		List<DBObject> docs = new ArrayList<DBObject>();
		LocationTransformer transformer = new LocationTransformer();

		int insertCounter = 0;

		for (Location location : locations) {
			docs.add((DBObject) transformer.transform(location));
			insertCounter++;

			if (insertCounter == batchSize) {
				collection.insert(docs);
				insertCounter = 0;
				docs = new ArrayList<DBObject>();
			}
		}

		// catch last docs
		if (insertCounter > 0 && !docs.isEmpty()) {
			collection.insert(docs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jimmyray.mongo.services.LocationService#deleteLocation(org.jimmyray
	 * .mongo .data.model.Location)
	 */
	@Override
	public void deleteLocation(Location location) {
		this.locationRepository.delete(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jimmyray.mongo.services.LocationService#deleteAll()
	 */
	@Override
	public void deleteAll() {
		this.locationRepository.deleteAll();
	}

	/**
	 * Sets the location repository.
	 * 
	 * @param locationRepository
	 *            the new location repository
	 */
	public void setLocationRepository(LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}

	/**
	 * Sets the mongo.
	 * 
	 * @param mongo
	 *            the new mongo
	 */
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public void setMongoOps(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	@Override
	public List<Location> findByCityAndState(String city, String state) {
		return this.locationRepository.findByCityAndState(city, state);
	}

	@Override
	public List<Location> findByGeoWithin(Circle circle) {
		return this.locationRepository.findByGeoWithin(circle.getCenter().getX(), circle.getCenter().getY(),
				circle.getRadius().getValue());
	}

	@Override
	public List<Location> findByCityAndStateAndZipCode(String city, String state, String zipCode) {
		return this.locationRepository.findByCityAndStateAndZipCode(city, state, zipCode);
	}

	@Override
	public List<Location> findNear(double lon, double lat, double distance) {
		return this.locationRepository.findByGeoNear(lon, lat, distance);
	}

	@Override
	public List<Location> findNear(Location location, double distanceInMiles) {
		return this.findNear(location.getLongitude(), location.getLatitude(), GeoUtils.milesToMeters(distanceInMiles));
	}

	@Override
	public GeoResults<Location> findNearPoint(Location location, double distanceInMiles) {

		Point point = new Point(location.getLongitude(), location.getLatitude());

		NearQuery query = NearQuery.near(point).maxDistance(new Distance(distanceInMiles, Metrics.MILES))
				.in(Metrics.MILES);
		// .distanceMultiplier(Metrics.MILES);

		GeoResults<Location> results = this.mongoOps.geoNear(query, Location.class);

		return results;
	}
}
