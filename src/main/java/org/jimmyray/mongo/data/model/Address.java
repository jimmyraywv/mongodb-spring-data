package org.jimmyray.mongo.data.model;

import java.text.MessageFormat;

import org.jimmyray.mongo.framework.ApplicationConstants;
import org.jimmyray.mongo.framework.Strings;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Address data model object.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class Address extends BaseMongoModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6472891307516456347L;
	@Field("line1")
	private String addressLine1;
	@Field("line2")
	private String addressLine2;
	// @Indexed(sparse = true)
	private String city;
	// @Indexed(sparse = true)
	@Field("cnty")
	private String county;
	// @Indexed(sparse = true)
	@Field("st")
	private String state;
	@Field("zip")
	private String zipCode;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}",
				this.getAddressLine1(), this.getAddressLine2(), this.getCity(),
				this.getCounty(), this.getState(), this.getZipCode());
	}

	public boolean equals(Object object) {
		if (!(object instanceof Address)) {
			return false;
		}
		Address address = (Address) object;
		if (this.toString().equals(address.toString())) {
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
		result = HASH_SALT * result
				+ Strings.generateIntHash(this.getAddressLine1());
		result = HASH_SALT * result
				+ Strings.generateIntHash(this.getAddressLine2());
		result = HASH_SALT * result + Strings.generateIntHash(this.getCity());
		result = HASH_SALT * result + Strings.generateIntHash(this.getState());
		result = HASH_SALT * result
				+ Strings.generateIntHash(this.getZipCode());
		return result;
	}
}
