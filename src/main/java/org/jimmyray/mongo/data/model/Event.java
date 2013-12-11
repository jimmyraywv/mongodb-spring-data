package org.jimmyray.mongo.data.model;

import java.util.Date;

import org.jimmyray.mongo.data.model.properties.EventProperties;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Event model class
 * 
 * @author jimmyray
 * @version 1.0
 */
public class Event extends BaseMongoModel {

	private static final long serialVersionUID = 8111255149333095102L;

	@Field(EventProperties.TIME_STAMP)
	private Date timeStamp;

	@Field(EventProperties.EVENT_LABEL)
	private Date label;

	@Field(EventProperties.EVENT_TEXT)
	private String text;
}
