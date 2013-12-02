package org.jimmyray.mongo.data.repository.custom;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

/**
 * Impl for custom repo utils
 * 
 * @author jimmyray
 * @version 1.0
 */
public abstract class RepositoryUtilsImpl implements RepositoryUtils {

	@Override
	public List<String> getCollectionJson(DBCollection collection) {

		List<String> list = new ArrayList<String>();
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			list.add(cursor.next().toString());
		}

		return list;
	}

}
