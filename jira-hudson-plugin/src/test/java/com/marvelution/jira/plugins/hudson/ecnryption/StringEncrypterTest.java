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

package com.marvelution.jira.plugins.hudson.ecnryption;

import static org.junit.Assert.*;

import org.junit.Test;

import com.marvelution.jira.plugins.hudson.encryption.StringEncrypter;

/**
 * String Encryption Test implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class StringEncrypterTest {

	/**
	 * Test the encryption of an <code>empty</code> {@link String}
	 */
	@Test
	public void testEncryptEmptyString() {
		assertEquals("", new StringEncrypter().encrypt(""));
		assertEquals("", new StringEncrypter().encrypt(null));
	}

	/**
	 * Test the decryption of an <code>empty</code> {@link String}
	 */
	@Test
	public void testDecryptEmptyString() {
		assertEquals("", new StringEncrypter().decrypt(""));
		assertEquals("", new StringEncrypter().decrypt(null));
	}

	/**
	 * Test the decryption of an encrypted {@link String}
	 */
	@Test
	public void testDecryptEncryptedString() {
		assertEquals("was-encrypted", new StringEncrypter().decrypt(new StringEncrypter().encrypt("was-encrypted")));
	}

}
