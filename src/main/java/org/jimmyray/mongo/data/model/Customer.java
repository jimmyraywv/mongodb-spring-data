package org.jimmyray.mongo.data.model;

import org.jimmyray.mongo.framework.Strings;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Customer model object with MongoDB meta-mapping annotations.
 * 
 * @author jimmyray
 * @version 1.0
 */
@Document(collection = "customers")
public class Customer extends BaseMongoModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7847084325516124863L;
	@Indexed(sparse = true, unique = true)
	private String customerId;
	@Indexed(sparse = true)
	private String customerName;
	@DBRef
	private CustomerAddress address;
	private String shippingMethod;
	private String terms;
	private float taxRate;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public CustomerAddress getAddress() {
		return address;
	}

	public void setAddress(CustomerAddress address) {
		this.address = address;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public float getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(float taxRate) {
		this.taxRate = taxRate;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()
				+ "|"
				+ this.getId()
				+ "|"
				+ Strings
						.nullValue(this.getCustomerId(), Strings.NO_DATA_FOUND)
				+ "|"
				+ Strings.nullValue(this.getCustomerName(),
						Strings.NO_DATA_FOUND)
				+ "|"
				+ Strings.nullValue(this.getShippingMethod(),
						Strings.NO_DATA_FOUND)
				+ "|"
				+ Strings.nullValue(this.getTerms(), Strings.NO_DATA_FOUND)
				+ "|"
				+ ((this.getTaxRate() == 0) ? Strings.NO_DATA_FOUND : this
						.getTaxRate())
				+ "|"
				+ ((null == this.getAddress()) ? Strings.NO_DATA_FOUND : this
						.getAddress().toString());
	}

}
