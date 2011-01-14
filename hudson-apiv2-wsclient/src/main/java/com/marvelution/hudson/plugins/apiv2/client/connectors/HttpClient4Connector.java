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

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.services.Query;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * Apache HTTPClient v4 implementation of the {@link Connector} interface.
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class HttpClient4Connector implements Connector {

	private final Host server;

	/**
	 * Default Constructor
	 * 
	 * @param server the {@link Host} to connect to
	 */
	public HttpClient4Connector(Host server) {
		this.server = server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <MODEL extends Model> ConnectorResponse execute(Query<MODEL> query) {
		DefaultHttpClient client = createClient();
		HttpGet get = createGetMethod(query);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return getConnectorResponseFromEntity(response);
			}
		} catch (IOException e) {
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * Method to create a {@link HttpGet}
	 * 
	 * @param <MODEL> the {@link Model} that is requested in the {@link Query}
	 * @param query the {@link Query} to execute
	 * @return the {@link HttpGet} object
	 */
	private <MODEL extends Model> HttpGet createGetMethod(Query<MODEL> query) {
		HttpGet get = new HttpGet(this.server.getHost() + query.getUrl());
		get.setHeader("Accept", "application/xml");
		return get;
	}

	/**
	 * Method to create the {@link HttpClient}
	 * 
	 * @return the {@link DefaultHttpClient}
	 */
	private DefaultHttpClient createClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		if (this.server.getUsername() != null) {
			client.getCredentialsProvider().setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(this.server.getUsername(), this.server.getPassword()));
		}
		return client;
	}

	/**
	 * Internal method to convert a {@link HttpResponse} into a {@link ConnectorResponse}
	 * 
	 * @param httpResponse the {@link HttpResponse} to convert
	 * @return the {@link ConnectorResponse}
	 * @throws ConnectionException in case of errors while creating the {@link ConnectorResponse}
	 */
	private ConnectorResponse getConnectorResponseFromEntity(HttpResponse httpResponse) throws ConnectionException {
		ConnectorResponse response = new ConnectorResponse();
		response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
		try {
			response.setResponseAsStream(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			throw new ConnectionException("Failed to copy the response stream", e);
		}
		return response;
	}
}
