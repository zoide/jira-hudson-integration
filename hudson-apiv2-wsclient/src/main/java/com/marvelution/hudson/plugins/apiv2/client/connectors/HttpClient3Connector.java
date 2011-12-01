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
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.services.Query;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;


/**
 * The Commons {@link HttpClient} {@link Connector} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class HttpClient3Connector implements Connector {

	private static final int MAX_TOTAL_CONNECTIONS = 40;
	private static final int MAX_HOST_CONNECTIONS = 4;

	private final Log log = LogFactory.getLog(HttpClient3Connector.class);
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
		httpClient = new HttpClient(connectionManager);
		configureCredentials();
		if (StringUtils.isNotBlank(System.getProperty("http.proxyHost"))) {
			log.debug("A HTTP Proxy is configured");
			System.setProperty("java.net.useSystemProxies", "true");
			Proxy proxy = chooseProxy();
			if (proxy.type() == Type.HTTP && proxy.address() instanceof InetSocketAddress) {
				// convert the socket address to an ProxyHost
	            final InetSocketAddress isa = (InetSocketAddress) proxy.address();
	            // assume default scheme (http)
	            ProxyHost proxyHost = new ProxyHost(getHost(isa), isa.getPort());
	            httpClient.getHostConfiguration().setProxyHost(proxyHost);
	            if (StringUtils.isNotBlank(System.getProperty("http.proxyUser"))) {
	            	String user = System.getProperty("http.proxyUser");
	            	String password = System.getProperty("http.proxyPassword");
					httpClient.getState().setProxyCredentials(new AuthScope(getHost(isa), isa.getPort()),
						new UsernamePasswordCredentials(user, password));
	            }
			}
		}
	}

	/**
	 * Configure the credentials of the {@link #server} in the {@link #httpClient}
	 */
	private void configureCredentials() {
		if (server.isSecured()) {
			log.debug("The Server is secured, set the preepmtive authentication header and credentials");
			httpClient.getParams().setAuthenticationPreemptive(true);
			Credentials defaultcreds = new UsernamePasswordCredentials(server.getUsername(),
				server.getPassword());
			httpClient.getState().setCredentials(getAuthScope(), defaultcreds);
		}
	}

	/**
	 * Get the {@link AuthScope} for the {@link #server}
	 * 
	 * @return the {@link AuthScope}
	 */
	private AuthScope getAuthScope() {
		String host = AuthScope.ANY_HOST;
		int port = AuthScope.ANY_PORT;
		try {
			final URI hostUri = new URI(server.getHost());
			host = hostUri.getHost();
			if (hostUri.getPort() > -1) {
				port = hostUri.getPort();
			} else if ("http".equalsIgnoreCase(hostUri.getScheme())) {
				port = 80;
			} else if ("https".equalsIgnoreCase(hostUri.getScheme())) {
				port = 443;
			}
		} catch (URISyntaxException e) {
			// Failed to parse the server host URI
			// Fall-back on preset defaults
			log.error("Failed to parse the host URI", e);
		}
		return new AuthScope(host, port, "realm");
	}

	/**
	 * Get the first available {@link Proxy} using the {@link ProxySelector} implementation
	 * 
	 * @return the first {@link Type#HTTP} proxy available, or {@link Proxy#NO_PROXY} if no
	 *         proxy is available
	 */
	private Proxy chooseProxy() {
		try {
			for (Proxy proxy : ProxySelector.getDefault().select(new URI(server.getHost()))) {
				if (proxy.type() == Type.HTTP) {
					return proxy;
				}
			}
		} catch (URISyntaxException e) {
			// Failed to parse the server host URI
			// Fallback to the default NO_PROXY
			log.error("Failed to parse the host URI", e);
		}
		return Proxy.NO_PROXY;
	}

	/**
	 * Obtains a host from an {@link InetSocketAddress}.
	 * 
	 * @param isa the socket address
	 * @return a host string, either as a symbolic name or as a literal IP address string
	 */
	protected String getHost(InetSocketAddress isa) {
		return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <MODEL extends Model> ConnectorResponse execute(Query<MODEL> query) {
		GetMethod method = null;
		String url = null;
		try {
			url = server.getHost() + URIUtil.encodeQuery(query.getUrl());
			method = new GetMethod(url);
			method.setRequestHeader("Accept", "application/xml");
			httpClient.executeMethod(method);
			return getConnectorResponseFromMethod(method);
		} catch (HttpException e) {
			log.error("Failed to execute the query: " + query.getUrl(), e);
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
