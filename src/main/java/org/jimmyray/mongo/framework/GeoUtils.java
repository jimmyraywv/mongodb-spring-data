package org.jimmyray.mongo.framework;

/**
 * Provide utilities for GEO operations.
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class GeoUtils {

	public static final double MILES_METERS_DIVISOR = 0.00062137;

	private GeoUtils() {

	}

	public static double milesToMeters(double miles) {
		return miles / GeoUtils.MILES_METERS_DIVISOR;
	}
}
