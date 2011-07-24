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

package com.marvelution.hudson.plugins.jirareporter;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteException;

/**
 * JIRA Site object used by the Notifier
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JIRASite {

	/**
	 * The Endpoint where the JIRA SOAP service can be found
	 */
	public static final String SERVICE_ENDPOINT = "rpc/soap/jirasoapservice-v2";

	public static final String SERVICE_ENDPOINT_WSDL = SERVICE_ENDPOINT + "?wsdl";

	public final String name;
	public final URL url;
	public final String username;
	public final String password;
	public final boolean supportsWikiStyle;

	/**
	 * Constructor
	 * 
	 * @param name the JIRA Site name
	 * @param url the JIRA Site {@link URL}
	 * @param username the username to use for Authentication with the JIRA Site
	 * @param password the password for the given username
	 * @param supportsWikiStyle flag is Wiki style texts are supported
	 */
	@DataBoundConstructor
	public JIRASite(String name, URL url, String username, String password, boolean supportsWikiStyle) {
		this.name = name;
		if (!url.toExternalForm().endsWith("/"))
		try {
			url = new URL(url.toExternalForm() + "/");
		} catch (MalformedURLException e) {
			// Ignore this, cannot happen any way
		}
		this.url = url;
		this.username = username;
		this.password = password;
		this.supportsWikiStyle = supportsWikiStyle;
	}

	/**
	 * Get the {@link JIRAClient} for this {@link JIRASite}
	 * 
	 * @return the {@link JIRAClient}
	 * @throws RemoteAuthenticationException
	 * @throws RemoteException
	 * @throws java.rmi.RemoteException
	 * @throws MalformedURLException
	 * @throws ServiceException
	 */
	public JIRAClient createClient() throws RemoteAuthenticationException, RemoteException, java.rmi.RemoteException,
			MalformedURLException, ServiceException {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return null;
		}
		JiraSoapServiceService jiraSoapServiceLocator = new JiraSoapServiceServiceLocator();
		JiraSoapService service = jiraSoapServiceLocator.getJirasoapserviceV2(new URL(url, SERVICE_ENDPOINT));
		return new JIRAClient(service, service.login(username, password), this);
	}
	
}
