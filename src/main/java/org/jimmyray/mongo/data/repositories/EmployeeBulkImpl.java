package org.jimmyray.mongo.data.repositories;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.data.transformers.EmployeeTransformer;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Impl for custom Employee bulk inserts
 * 
 * @author jimmyray
 * @version 3.0
 */
public abstract class EmployeeBulkImpl implements EmployeeBulk {
	private static Logger log = LoggerFactory.getLogger(EmployeeBulkImpl.class);

	/**
	 * Bulk inserts docs of a specific batch size.
	 * 
	 * @param employees
	 *            List<Employee>
	 * @param batchSize
	 *            int
	 */
	// @Override
	public void bulkInsert(List<Employee> employees, int batchSize) {
		MongoClient mongo = (MongoClient) SpringBeanFactory.getBean("mongoClient");
		MongoDatabase db = mongo.getDatabase((String) SpringBeanFactory.getBean("dbName"));

		MongoCollection<DBObject> collection = db.getCollection(EmployeeProperties.COLLECTION, DBObject.class);
		List<DBObject> docs = new ArrayList<DBObject>();
		EmployeeTransformer transformer = new EmployeeTransformer();

		int insertCounter = 0;
		try {
			for (Employee employee : employees) {
				docs.add((DBObject)transformer.transform(employee));
				insertCounter++;

				if (insertCounter == batchSize) {
					collection.insertMany(docs);

					insertCounter = 0;
					docs = new ArrayList<DBObject>();
				}
			}

			// catch last docs
			if (insertCounter > 0 && !docs.isEmpty()) {
				collection.insertMany(docs);
			}
		} catch (MongoException me) {
			log.error(ExceptionUtils.getFullStackTrace(me));
		}

	}

}
