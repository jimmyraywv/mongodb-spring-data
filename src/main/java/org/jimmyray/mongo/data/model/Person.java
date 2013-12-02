package org.jimmyray.mongo.data.model;

import java.util.Date;

import org.jimmyray.mongo.data.model.properties.PersonProperties;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Person data model object.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class Person extends BaseMongoModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3257043035288218260L;
	@Field(PersonProperties.LAST_NAME)
	private String lastName;
	@Field(PersonProperties.FIRST_NAME)
	private String firstName;
	@Field(PersonProperties.GENDER)
	private String gender;
	@Field(PersonProperties.BIRTH_DATE)
	private Date birthDate;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public final Date getBirthDate() {
		return (null != this.birthDate) ? new Date(this.birthDate.getTime())
				: null;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = (Date) birthDate.clone();
	}

}
