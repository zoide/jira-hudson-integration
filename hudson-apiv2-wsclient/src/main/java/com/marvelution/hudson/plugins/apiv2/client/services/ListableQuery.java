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

package com.marvelution.hudson.plugins.apiv2.client.services;

import com.marvelution.hudson.plugins.apiv2.client.connectors.Connector;
import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * HTTP GET Listable Query interface
 * Interface for all Queries that can be executed using a {@link Connector}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public interface ListableQuery<MODEL extends Model, LISTMODEL extends ListableModel<MODEL>> extends Query<MODEL> {

	/**
	 * Get the {@link Model} class that this Query returns.
	 * This is used to get the correct Unmarshaller.
	 * 
	 * @return the {@link Model} class
	 */
	Class<LISTMODEL> getListableModelClass();

}
