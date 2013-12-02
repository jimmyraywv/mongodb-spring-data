package org.jimmyray.mongo.data.model;

import java.io.Serializable;
import java.text.MessageFormat;

import org.jimmyray.mongo.framework.ApplicationConstants;
import org.jimmyray.mongo.framework.Strings;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Department model object.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class Department implements Serializable {
	private static final long serialVersionUID = 2618154643598466552L;
	private String name;
	private String id;
	@Field("mgrId")
	private String managerId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}, {1}, {2}", this.getId(),
				this.getName(), this.getManagerId());
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Department)) {
			return false;
		}
		Department department = (Department) object;
		if (this.toString().equals(department.toString())) {
			return true;
		}
		return false;
	}

	private static final int HASH_SEED = ApplicationConstants.HASH_SEED;
	private static final int HASH_SALT = ApplicationConstants.HASH_SALT;

	@Override
	public int hashCode() {
		int result = 0;
		result = HASH_SEED;
		result = HASH_SALT * result + Strings.generateIntHash(this.getId());
		result = HASH_SALT * result + Strings.generateIntHash(this.getName());
		result = HASH_SALT * result
				+ Strings.generateIntHash(this.getManagerId());
		return result;
	}

}
