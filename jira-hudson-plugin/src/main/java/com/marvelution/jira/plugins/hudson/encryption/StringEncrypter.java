/*
 * Licensed to Marvelution under one or more contributor license 
 * agreements.  See the NOTICE file distributed with this work 
 * for additional information regarding copyright ownership.
 * Marvelution licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.marvelution.jira.plugins.hudson.encryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

/**
 * String Encryption implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class StringEncrypter {

	private static final Logger LOGGER = Logger.getLogger(StringEncrypter.class);

	private static final String DEFAULT_ENCRYPTION_KEY = "Hudson Password Encryption - Sp6yeCas+ehA#a=_spuSPaVe";

	private static final String UNICODE_FORMAT = "UTF8";

	private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";

	protected Cipher cipher;

	protected SecretKeyFactory keyFactory;

	protected DESedeKeySpec keySpec;

	/**
	 * Constructor
	 */
	public StringEncrypter() {
		try {
			keySpec = new DESedeKeySpec(DEFAULT_ENCRYPTION_KEY.getBytes(UNICODE_FORMAT));
			keyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
			cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
		} catch (Exception e) {
			LOGGER.fatal("Failed to initilise String Encryption classes. Reason: " + e.getMessage(), e);
			throw new StringEncryptionException("Failed to initilise String Encryption classes. Reason: "
				+ e.getMessage(), e);
		}
	}

	/**
	 * Encrypt a given {@link String}
	 * 
	 * @param stringToEncrypt the {@link String} to encrypt
	 * @return the encrypted {@link String}
	 */
	public String encrypt(String stringToEncrypt) {
		if (stringToEncrypt == null || stringToEncrypt.length() == 0) {
			return "";
		}
		try {
			initiliseCipher(Cipher.ENCRYPT_MODE);
			return new String(Base64.encodeBase64(cipher.doFinal(stringToEncrypt.getBytes(UNICODE_FORMAT))));
		} catch (Exception e) {
			LOGGER.error("Failed to encrypt provided String. Reason: " + e.getMessage(), e);
			throw new StringEncryptionException("Failed to encrypt provided String. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Decrypt a given {@link String}
	 * 
	 * @param stringToDecrypt the {@link String} to Decrypt
	 * @return the Decrypted {@link String}
	 */
	public String decrypt(String stringToDecrypt) {
		if (stringToDecrypt == null || stringToDecrypt.length() == 0) {
			return "";
		}
		try {
			initiliseCipher(Cipher.DECRYPT_MODE);
			return new String(cipher.doFinal(Base64.decodeBase64(stringToDecrypt.getBytes())));
		} catch (Exception e) {
			LOGGER.error("Failed to decrypt provided String. Reason: " + e.getMessage(), e);
			throw new StringEncryptionException("Failed to decrypt provided String. Reason: " + e.getMessage(), e);
		}
	}

	/**
	 * Initialise the {@link Cipher} implementation
	 * @param mode the {@link Cipher} operation mode
	 * 
	 * @throws InvalidKeySpecException in case of {@link SecretKey} generation exception
	 * @throws InvalidKeyException in case the {@link Cipher} cannot be initialised
	 */
	private void initiliseCipher(int mode) throws InvalidKeySpecException, InvalidKeyException {
		final SecretKey secretKey = keyFactory.generateSecret(keySpec);
		cipher.init(mode, secretKey);
	}

}
