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

package com.marvelution.hudson.plugins.apiv2.client.connectors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

/**
 * Response Object that is returned by a {@link Connector}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public final class ConnectorResponse {

	private int statusCode;
	private String reasonPhrase;
	private ByteArrayOutputStream outputStream;

	/**
	 * Getter for statusCode
	 * 
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Setter for statusCode
	 * 
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Getter for reasonPhrase
	 * 
	 * @return the reasonPhrase
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * Setter for reasonPhrase
	 * 
	 * @param reasonPhrase the reasonPhrase to set
	 */
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Copy the given {@link InputStream} response
	 * 
	 * @param input the {@link InputStream} to copy
	 * @throws IOException in case of errors during the copying
	 */
	public void setResponseAsStream(InputStream input) throws IOException {
		if (outputStream == null) {
			outputStream = new ByteArrayOutputStream();
			IOUtils.copy(input, outputStream);
		} else {
			throw new IllegalStateException("Cannot re-initialize the response stream");
		}
	}

	/**
	 * Get the response as a {@link InputStream}
	 * 
	 * @return the {@link InputStream}
	 */
	public InputStream getResponseAsStream() {
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	/**
	 * Get the response as a {@link Reader}
	 * 
	 * @return the {@link Reader}
	 */
	public Reader getResponseAsReader() {
		return new InputStreamReader(getResponseAsStream());
	}

}
