package org.jimmyray.mongo.data.model;

import org.jimmyray.mongo.framework.Strings;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Class Location.
 */
@Document(collection = "locations")
public class Location extends BaseMongoModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8962529524396906575L;

	/** The coords. */
	private double[] coords;

	/** The population. */
	private int population;

	/** The city. */
	@Indexed
	private String city;

	/** The state. */
	@Indexed
	private String state;

	/** The zip code. */
	private String zipCode;

	/** The time zone. */
	private int timeZone;

	/** The dst observed. */
	private boolean dstObserved;

	/**
	 * Gets the coords.
	 * 
	 * @return the coords
	 */
	public final double[] getCoords() {
		double[] copy = new double[2];
		System.arraycopy(this.coords, 0, copy, 0, 2);
		return copy;
	}

	/**
	 * Sets the coords.
	 * 
	 * @param coords
	 *            the new coords
	 */
	public void setCoords(double[] coords) {
		this.coords = coords.clone();
	}

	/**
	 * Gets the population.
	 * 
	 * @return the population
	 */
	public int getPopulation() {
		return population;
	}

	/**
	 * Sets the population.
	 * 
	 * @param population
	 *            the new population
	 */
	public void setPopulation(int population) {
		this.population = population;
	}

	/**
	 * Gets the city.
	 * 
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 * 
	 * @param city
	 *            the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the new state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the zip code.
	 * 
	 * @return the zip code
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the zip code.
	 * 
	 * @param zipCode
	 *            the new zip code
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Gets the time zone.
	 * 
	 * @return the time zone
	 */
	public int getTimeZone() {
		return timeZone;
	}

	/**
	 * Sets the time zone.
	 * 
	 * @param timeZone
	 *            the new time zone
	 */
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * Checks if is dst observed.
	 * 
	 * @return true, if is dst observed
	 */
	public boolean isDstObserved() {
		return dstObserved;
	}

	/**
	 * Sets the dst observed.
	 * 
	 * @param dstObserved
	 *            the new dst observed
	 */
	public void setDstObserved(boolean dstObserved) {
		this.dstObserved = dstObserved;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public double getLatitude() {
		return this.getCoords()[1];
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public double getLongitude() {
		return this.getCoords()[0];
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "|"
				+ Strings.nullValue(this.getCity(), Strings.NO_DATA_FOUND)
				+ "|"
				+ Strings.nullValue(this.getState(), Strings.NO_DATA_FOUND)
				+ "|" + Strings.nullValue(this.zipCode, Strings.NO_DATA_FOUND);
	}
}
