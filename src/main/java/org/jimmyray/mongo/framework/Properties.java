package org.jimmyray.mongo.framework;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Properties exposes message bundle access to the rest of the application.
 * 
 * @author jimmyray
 * @version 1.0
 */
public final class Properties {
	private static final String BUNDLE_NAME = "config"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Properties() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
