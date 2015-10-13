package org.jimmyray.mongo.data.repositories;

import java.util.List;

import com.mongodb.DBCollection;

/**
 * The Interface RepositoryUtils is used to create a custom repository.
 */
public interface RepositoryUtils {

	/**
	 * Gets the collection json.
	 * 
	 * @param collection
	 *            the collection
	 * @return the collection json
	 */
	List<String> getCollectionJson(DBCollection collection);
}
