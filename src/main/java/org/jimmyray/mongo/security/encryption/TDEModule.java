package org.jimmyray.mongo.security.encryption;

import org.jasypt.util.text.StrongTextEncryptor;

/**
 * Provides simple TDE functionality that should be called by a service layer.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class TDEModule {

	/*
	 * This should be replaced with a more secure secret token
	 */
	private static String PASSWORD = "SECRET";

	/**
	 * encrypt
	 * 
	 * @param plainText
	 * @return String encrypted data
	 */
	public String encrypt(String plainText) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(TDEModule.PASSWORD);

		return textEncryptor.encrypt(plainText);
	}

	/**
	 * decrypt
	 * 
	 * @param encryptedText
	 * @return
	 */
	public String decrypt(String encryptedText) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(TDEModule.PASSWORD);

		return textEncryptor.decrypt(encryptedText);
	}

}
