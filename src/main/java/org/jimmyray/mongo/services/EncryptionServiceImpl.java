package org.jimmyray.mongo.services;

import org.jimmyray.mongo.security.encryption.TDEModule;

/**
 * Impl of Encrypt/Decrypt service
 * 
 * @author jimmyray
 * @version 1.0
 */
public class EncryptionServiceImpl implements EncryptionService {

	private TDEModule tdeModule;
	
	/**
	 * Encrypt
	 * 
	 * @param String
	 *            plainText
	 */
	@Override
	public String encrypt(String plainText) {
		return this.tdeModule.encrypt(plainText);
	}

	/**
	 * Decrypt
	 * 
	 * @param String
	 *            encryptedString
	 */
	@Override
	public String decrypt(String encryptedText) {
		return this.tdeModule.decrypt(encryptedText);
	}
	
	public void setTdeModule(TDEModule tdeModule) {
		this.tdeModule = tdeModule;
	}
}
