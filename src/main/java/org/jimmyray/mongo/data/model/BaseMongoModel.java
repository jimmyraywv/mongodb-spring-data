package org.jimmyray.mongo.data.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/**
 * The Class BaseMongoModel.
 */
public abstract class BaseMongoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7588350609802434712L;
	
	public BaseMongoModel() {
		
	}
	
	protected BaseMongoModel(String id) {
		this.id = id;
	}

	/** The id. */
	@Id
	private String id;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
}
