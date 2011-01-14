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

package com.marvelution.hudson.plugins.apiv2.client;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.marvelution.hudson.plugins.apiv2.client.connectors.Connector;
import com.marvelution.hudson.plugins.apiv2.client.connectors.ConnectorFactory;
import com.marvelution.hudson.plugins.apiv2.client.connectors.ConnectorResponse;
import com.marvelution.hudson.plugins.apiv2.client.services.ListableQuery;
import com.marvelution.hudson.plugins.apiv2.client.services.Query;
import com.marvelution.hudson.plugins.apiv2.client.unmarshallers.Unmarshallers;
import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;


/**
 * Client implementation for connecting to the Hudson API version 2 plugin
 * 
 * By default the client implementation will use the commons-httpclient for the connection between the client and the server.
 * To instantiate a HudsonClient using commons-httpclient
 * <code>
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
HudsonClient hudson = HudsonClient.create("http://localhost:8080");
 * </code>
 * If the security is enabled, you have to define the credentials:
 * <code>
HudsonClient hudson = HudsonClient.create("http://localhost:8080", "login", "password");
 * </code>
 * When using the lib Commons HttpClient 4.0, the constructor is slightly different:
 * <code>
HudsonClient hudson = new HudsonClient(new HttpClient4Connector(new Host("http://localhost:8080")));
 * </code>
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public class HudsonClient {

	private Connector connector;

	/**
	 * Default Constructor
	 * 
	 * @param connector the {@link Connector} implementation
	 */
	public HudsonClient(Connector connector) {
		this.connector = connector;
	}

	/**
	 * Getter for the {@link Connector}
	 * 
	 * @return the {@link Connector} implementation
	 */
	public Connector getConnector() {
		return connector;
	}

	/**
	 * Method to find a {@link Model} type using its {@link Query}
	 * 
	 * @param <MODEL> the {@link Model} type
	 * @param query the {@link Query} implementation for the {@link Model} type
	 * @return the {@link Model} response from the Hudson server, may be <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public <MODEL extends Model> MODEL find(Query<MODEL> query) {
		ConnectorResponse response = connector.execute(query);
		if (response != null) {
			try {
				Unmarshaller unmarshaller = Unmarshallers.forModel(query.getModelClass());
				return (MODEL) unmarshaller.unmarshal(response.getResponseAsStream());
			} catch (JAXBException e) {
			}
		}
		return null;
	}

	/**
	 * Method to find all the {@link Model} types using its {@link Query}
	 * 
	 * @param <LISTMODEL> the {@link Model} type
	 * @param query the {@link Query} implementation for the {@link Model} type
	 * @return the {@link List} of {@link Model} objects from the response from the Hudson server, may be
	 *         <code>null</code> or an <code>empty</code> {@link List}
	 */
	@SuppressWarnings("unchecked")
	public <MODEL extends Model, LISTMODEL extends ListableModel<MODEL>> LISTMODEL findAll(ListableQuery<MODEL,
			LISTMODEL> query) {
		ConnectorResponse response = connector.execute(query);
		if (response != null) {
			try {
				Unmarshaller unmarshaller = Unmarshallers.forModel(query.getListableModelClass());
				return (LISTMODEL) unmarshaller.unmarshal(response.getResponseAsStream());
			} catch (JAXBException e) {
			}
		}
		return null;
	}

	/**
	 * Static method to create a {@link HudsonClient} for the unsecured host base-url given using a
	 * Commons-HttpClient version 3 connector
	 * 
	 * @param host the host base-url to connect to
	 * @return the {@link HudsonClient} object
	 */
	public static HudsonClient create(String host) {
		return new HudsonClient(ConnectorFactory.create(new Host(host)));
	}

	/**
	 * Static method to create a {@link HudsonClient} for the secured host base-url given using a Commons-HttpClient version 3 connector
	 * 
	 * @param host the host base-url to connect to
	 * @param username the username
	 * @param password the password
	 * @return the {@link HudsonClient} object
	 */
	public static HudsonClient create(String host, String username, String password) {
		return new HudsonClient(ConnectorFactory.create(new Host(host, username, password)));
	}

}
