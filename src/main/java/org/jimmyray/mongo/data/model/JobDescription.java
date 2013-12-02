package org.jimmyray.mongo.data.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Job Description model object.
 * 
 * @author jimmyray
 * @version 1.0
 */
@Document(collection = "jobDescriptions")
public class JobDescription implements Serializable {

	private static final long serialVersionUID = -2842700857597825428L;

	@Id
	private String id;
	@Indexed(sparse = true, unique = true)
	private String title;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
