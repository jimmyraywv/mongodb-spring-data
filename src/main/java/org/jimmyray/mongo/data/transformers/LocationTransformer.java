package org.jimmyray.mongo.data.transformers;

import java.util.Map;

import org.bson.Transformer;
import org.jimmyray.mongo.data.loaders.LocationFactory;
import org.jimmyray.mongo.data.model.Location;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * The Class LocationTransformer.
 * 
 * @author jimmyray
 */
public class LocationTransformer implements Transformer {
	/**
	 * Parse into JSON.
	 * 
	 * @param employee
	 *            the employee
	 * @return the dB object
	 */
	public DBObject transform(Object location) {

		Map<String, Object> map = LocationFactory.getMap((Location) location);
		DBObject dbObject = new BasicDBObject(map);

		return dbObject;
	}
}
