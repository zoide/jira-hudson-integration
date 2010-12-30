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
import java.io.InputStream;

import org.apache.commons.httpclient.methods.GetMethod;
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
	public <MODEL extends Model> InputStream execute(Query<MODEL> query) {
		DefaultHttpClient client = createClient();
		HttpGet get = createGetMethod(query);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (response.getStatusLine().getStatusCode() == 200) {
					return entity.getContent();
				} else if (response.getStatusLine().getStatusCode() != 404) {
					throw new ConnectionException("HTTP error: " + response.getStatusLine().getStatusCode() + ", msg: "
						+ response.getStatusLine().getReasonPhrase() + ", query: " + get.toString());
				}
			}
		} catch (IOException e) {
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * Method to create a {@link GetMethod}
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
}
