package org.jimmyray.mongo.data.converters;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

/**
 * Simple String converter for DBObjects
 * 
 * @author jimmyray
 * @Version 1.0
 */
public class DBObjectJsonReadConverter implements Converter<DBObject, String> {
	public String convert(DBObject source) {
		if (null != source) {
			source.removeField("_class");
			return source.toString();
		}
		return null;
	}
}
