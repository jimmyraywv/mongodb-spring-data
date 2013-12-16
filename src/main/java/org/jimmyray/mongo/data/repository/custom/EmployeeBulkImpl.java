package org.jimmyray.mongo.data.repository.custom;

import java.util.ArrayList;
import java.util.List;

import org.jimmyray.mongo.data.model.Employee;
import org.jimmyray.mongo.data.model.properties.EmployeeProperties;
import org.jimmyray.mongo.data.transformers.EmployeeTransformer;
import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

/**
 * Impl for custom Employee bulk inserts
 * 
 * @author jimmyray
 * @version 1.0
 */
public abstract class EmployeeBulkImpl implements EmployeeBulk {
	private static Logger log = LoggerFactory.getLogger(EmployeeBulkImpl.class);

	/**
	 * Bulk inserts docs of a spepcifc batch size.
	 * 
	 * @param employees
	 *            List<Employee>
	 * @param batchSize
	 *            int
	 */
	@Override
	public void bulkInsert(List<Employee> employees, int batchSize) {
		Mongo mongo = (Mongo) SpringBeanFactory.getBean("mongo");
		DB db = mongo.getDB((String) SpringBeanFactory.getBean("dbName"));

		DBCollection collection = db
				.getCollection(EmployeeProperties.COLLECTION);
		List<DBObject> docs = new ArrayList<DBObject>();
		EmployeeTransformer transformer = new EmployeeTransformer();
		WriteResult result = null;

		int insertCounter = 0;

		for (Employee employee : employees) {
			docs.add((DBObject) transformer.transform(employee));
			insertCounter++;

			if (insertCounter == batchSize) {
				result = collection.insert(docs);
				if (log.isDebugEnabled()) {
					if (result.getError() != null) {
						log.debug(result.getError());
					} else {
						log.debug(result.toString());
					}
				}

				insertCounter = 0;
				docs = new ArrayList<DBObject>();
			}
		}

		// catch last docs
		if (insertCounter > 0 && !docs.isEmpty()) {
			collection.insert(docs);
		}

	}

}
