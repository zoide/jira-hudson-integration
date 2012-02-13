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
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.services.Query;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * Apache HTTPClient v4 implementation of the {@link Connector} interface.
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class HttpClient4Connector implements Connector {

	private final Log log = LogFactory.getLog(HttpClient4Connector.class);
	private final Host server;

	private BasicHttpContext localContext = null;

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
			HttpResponse response;
			if (localContext != null) {
				log.debug("Executing the query: " + query.getUrl() + " using the localContext with the credentials");
				response = client.execute(get, localContext);
			} else {
				log.debug("Executing the query: " + query.getUrl());
				response = client.execute(get);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return getConnectorResponseFromEntity(response);
			}
		} catch (IOException e) {
			log.error("Failed to execute the query: " + query.getUrl(), e);
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
		HttpGet get = new HttpGet(server.getHost() + query.getUrl());
		get.setHeader("Accept", query.getAcceptHeader());
		get.getParams().setParameter("http.connection.timeout", TIMEOUT_MS);
		get.getParams().setParameter("http.socket.timeout", TIMEOUT_MS);
		return get;
	}

	/**
	 * Method to create the {@link HttpClient}
	 * 
	 * @return the {@link DefaultHttpClient}
	 */
	private DefaultHttpClient createClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		if (server.isSecured()) {
			log.debug("The Server is secured, create the BasicHttpContext to handle preemptive authentication.");
			client.getCredentialsProvider().setCredentials(getAuthScope(),
				new UsernamePasswordCredentials(server.getUsername(), server.getPassword()));
			localContext = new BasicHttpContext();
            // Generate BASIC scheme object and stick it to the local execution context
            BasicScheme basicAuth = new BasicScheme();
            localContext.setAttribute("preemptive-auth", basicAuth);
            // Add as the first request intercepter
            client.addRequestInterceptor(new PreemptiveAuth(), 0);
		}
		if (StringUtils.isNotBlank(System.getProperty("http.proxyHost"))
			|| StringUtils.isNotBlank(System.getProperty("https.proxyHost"))) {
			log.debug("A System HTTP(S) proxy is set, set the ProxySelectorRoutePlanner for the client");
			System.setProperty("java.net.useSystemProxies", "true");
			// Set the ProxySelectorRoute Planner to automatically select the system proxy host if set.
			client.setRoutePlanner(new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(),
				ProxySelector.getDefault()));
		}
		return client;
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
			log.error("Failed to parse the Server host url to an URI", e);
		}
		return new AuthScope(host, port, "realm");
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

	/**
	 * {@link HttpRequestInterceptor} implementation for preemptive authentication
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 * 
	 * @since 4.2.0
	 */
	static class PreemptiveAuth implements HttpRequestInterceptor {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
			// If no auth scheme available yet, try to initialize it preemptively
			if (authState.getAuthScheme() == null) {
				AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
				CredentialsProvider credsProvider =
					(CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if (authScheme != null) {
					Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(),
						targetHost.getPort(), "realm"));
					if (creds == null) {
						throw new HttpException("No credentials for preemptive authentication");
					}
					authState.setAuthScheme(authScheme);
					authState.setCredentials(creds);
				}
			}
		}
	}

}
