package org.jimmyray.mongo.data.model;

import java.text.MessageFormat;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Customer Address model object with MongoDB meta-mapping annotations.
 * 
 * @author jimmyray
 * @version 1.0
 */
@Document(collection = "customerAddresses")
public class CustomerAddress extends Address {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2216701293464182114L;
	@Indexed(sparse = true)
	private String country;
	private String phone;
	private String fax;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public String toString() {
		return MessageFormat.format(
				"{0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}", this.getId(),
				this.getAddressLine1(), this.getAddressLine2(), this.getCity(),
				this.getCounty(), this.getState(), this.getZipCode(),
				this.getPhone(), this.getFax());
	}

}
