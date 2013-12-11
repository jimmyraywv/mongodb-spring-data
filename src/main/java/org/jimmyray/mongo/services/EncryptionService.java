package org.jimmyray.mongo.services;

/**
 * Encrypt/Decrypt Service Interface
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface EncryptionService {

	String encrypt(String plainText);

	String decrypt(String encryptedText);

}
