package org.jimmyray.mongo.data.transformers;

import java.util.Map;

import org.bson.Transformer;
import org.jimmyray.mongo.data.loaders.EmployeeFactory;
import org.jimmyray.mongo.data.model.Employee;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * The Class EmployeeTransformer.
 * 
 * @author jimmyray
 */
public class EmployeeTransformer implements Transformer {

	public Object transform(Object object) {

		Employee employee = (Employee) object;

		Map<String, Object> map = EmployeeFactory.getMap(employee);
		DBObject dbObject = new BasicDBObject(map);

		return dbObject;
	}
}
