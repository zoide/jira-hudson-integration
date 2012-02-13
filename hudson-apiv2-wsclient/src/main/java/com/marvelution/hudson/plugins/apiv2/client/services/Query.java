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

import org.apache.commons.codec.net.URLCodec;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * HTTP GET Query interface
 * Interface for all Queries that can be executed
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
public interface Query<MODEL extends Model> {

	/**
	 * {@link URLCodec} object used for URL encoding
	 */
	URLCodec URLCODEC = new URLCodec();

	/**
	 * Get the {@link QueryType} of this query
	 * 
	 * @return the {@link QueryType}
	 * @since 4.1.0
	 */
	QueryType getQueryType();

	/**
	 * Get the URL string where the query can be executed at
	 * 
	 * @return the URL, should start with a slash
	 */
	String getUrl();

	/**
	 * Get the Data f the query, usually used in POST and PUT queryies
	 * 
	 * @param <T> the type of the data object
	 * @return the data obejct
	 * @since 4.1.0
	 */
	<T> T getData();

	/**
	 * Getter for the Accept header
	 * 
	 * @return the Accept Header, may be <code>null</code>
	 */
	String getAcceptHeader();

	/**
	 * Get the {@link Model} class that this Query returns.
	 * This is used to get the correct Unmarshaller.
	 * 
	 * @return the {@link Model} class
	 */
	Class<MODEL> getModelClass();

}
