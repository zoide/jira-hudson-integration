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

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.URIUtil;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.services.Query;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;


/**
 * The Commons {@link HttpClient} {@link Connector} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class HttpClient3Connector implements Connector {

	private static final int TIMEOUT_MS = 30000;

	private static final int MAX_TOTAL_CONNECTIONS = 40;

	private static final int MAX_HOST_CONNECTIONS = 4;

	private final Host server;

	private HttpClient httpClient;

	/**
	 * Default constructor
	 * 
	 * @param server the {@link Host} to connect to
	 */
	public HttpClient3Connector(Host server) {
		this.server = server;
		createClient();
	}

	/**
	 * Constructor for a specific {@link Host} and  pre-configured {@link HttpClient}
	 * 
	 * @param server the {@link Host} to connect to
	 * @param httpClient the {@link HttpClient} to use for the connection
	 */
	public HttpClient3Connector(Host server, HttpClient httpClient) {
		this.httpClient = httpClient;
		this.server = server;
	}

	/**
	 * Create the {@link HttpClient} object
	 */
	private void createClient() {
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setConnectionTimeout(TIMEOUT_MS);
		params.setDefaultMaxConnectionsPerHost(MAX_HOST_CONNECTIONS);
		params.setMaxTotalConnections(MAX_TOTAL_CONNECTIONS);
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.setParams(params);
		this.httpClient = new HttpClient(connectionManager);
		configureCredentials();
	}

	/**
	 * Configure the credentials of the {@link #server} in the {@link #httpClient}
	 */
	private void configureCredentials() {
		if (this.server.getUsername() != null) {
			this.httpClient.getParams().setAuthenticationPreemptive(true);
			Credentials defaultcreds = new UsernamePasswordCredentials(this.server.getUsername(),
				this.server.getPassword());
			this.httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <MODEL extends Model> ConnectorResponse execute(Query<MODEL> query) {
		GetMethod method = null;
		String url = null;
		try {
			url = this.server.getHost() + URIUtil.encodeQuery(query.getUrl());
			method = new GetMethod(url);
			method.setRequestHeader("Accept", "application/xml");
			this.httpClient.executeMethod(method);
			return getConnectorResponseFromMethod(method);
		} catch (HttpException e) {
		} catch (IOException e) {
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}

	/**
	 * Internal method to convert a {@link HttpMethod} into a {@link ConnectorResponse}
	 * 
	 * @param method the {@link HttpMethod} to convert
	 * @return the {@link ConnectorResponse}
	 * @throws ConnectionException in case of errors while creating the {@link ConnectorResponse}
	 */
	private ConnectorResponse getConnectorResponseFromMethod(HttpMethod method) throws ConnectionException {
		ConnectorResponse response = new ConnectorResponse();
		response.setStatusCode(method.getStatusCode());
		response.setReasonPhrase(method.getStatusText());
		try {
			response.setResponseAsStream(method.getResponseBodyAsStream());
		} catch (IOException e) {
			throw new ConnectionException("Failed to copy the response stream", e);
		}
		return response;
	}

}
