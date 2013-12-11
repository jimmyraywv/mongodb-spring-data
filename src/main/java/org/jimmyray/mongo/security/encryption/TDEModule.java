package org.jimmyray.mongo.security.encryption;

import org.jasypt.util.text.StrongTextEncryptor;

/**
 * Provides simple TDE functionality
 * 
 * @author jimmyray
 * @version 1.0
 */
public class TDEModule {

	private static String PASSWORD = "SECRET";
	
	public String encrypt(String plainText) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(TDEModule.PASSWORD);

		return textEncryptor.encrypt(plainText);
	}
	
	public String decrypt(String encryptedText) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(TDEModule.PASSWORD);

		return  textEncryptor.decrypt(encryptedText);
	}

}
