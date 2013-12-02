package org.jimmyray.mongo.data.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * SpecialEmployee extends Employee to change the MongoDB collection.
 * 
 * @author jimmyray
 * @version 1.0
 */
@Document(collection = "specialEmployees")
public class SpecialEmployee extends Employee {
	private static final long serialVersionUID = -4594057674494315230L;

}
