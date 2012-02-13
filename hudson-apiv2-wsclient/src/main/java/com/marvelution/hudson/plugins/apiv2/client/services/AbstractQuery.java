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

import org.apache.commons.codec.EncoderException;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;

/**
 * Abstract {@link Query} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @param <MODEL> the {@link Model} type
 */
public abstract class AbstractQuery<MODEL extends Model> implements Query<MODEL> {

	private final Class<MODEL> modelClass;
	private final QueryType type;

	/**
	 * Protected constructor so this class must be subclassed
	 *  
	 * @param modelClass the {@link Class}
	 */
	protected AbstractQuery(Class<MODEL> modelClass, QueryType type) {
		this.modelClass = modelClass;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final QueryType getQueryType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getUrl() {
		return "/apiv2/" + getSpecificUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getData() {
		// Default implementation for the Queries. Most don't need this as they are GET Queries
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAcceptHeader() {
		return "application/xml";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<MODEL> getModelClass() {
		return modelClass;
	}

	/**
	 * Helper method to url encode a given {@link String}
	 * 
	 * @param url the url {@link String} to encode
	 * @return the encoded url
	 */
	protected String urlEncode(String url) {
		try {
			return URLCODEC.encode(url).replace("+", "%20");
		} catch (EncoderException e) {
			return url;
		}
	}

	/**
	 * Helper method to add a parameter to the URL builder
	 * 
	 * @param url the {@link StringBuilder} with the current URL
	 * @param paramKey the new parameter key
	 * @param paramValue the parameter value, this will be URL Encoded before adding it to the URL
	 * @see #addUrlParameter(StringBuilder, String, Object)
	 * @see #urlEncode(String)
	 */
	protected void addUrlParameter(StringBuilder url, String paramKey, String paramValue) {
		if (paramKey != null) {
			url.append(paramKey).append("=").append(urlEncode(paramValue)).append("&");
		}
	}

	/**
	 * Helper method to add a parameter to the URL builder
	 * 
	 * @param url the {@link StringBuilder} with the current URL
	 * @param paramKey the new parameter key
	 * @param paramValue the parameter value
	 */
	protected void addUrlParameter(StringBuilder url, String paramKey, Object paramValue) {
		if (paramKey != null) {
			url.append(paramKey).append("=").append(paramValue).append("&");
		}
	}

	/**
	 * Helper method to add a parameter with value array to the URL builder
	 * 
	 * @param url the {@link StringBuilder} with the current URL
	 * @param paramKey the new parameter key, <code>[]</code> will be added after the key to make it an array key
	 * @param paramValues the value array to add, each value will be URL encoded
	 * @see #addUrlParameter(StringBuilder, String, String)
	 */
	protected void addUrlParameter(StringBuilder url, String paramKey, String[] paramValues) {
		String arrayKey = paramKey + "[]";
		for (String value : paramValues) {
			addUrlParameter(url, arrayKey, value);
		}
	}

	/**
	 * Helper method to add a parameter with value array to the URL builder
	 * 
	 * @param url the {@link StringBuilder} with the current URL
	 * @param paramKey the new parameter key, <code>[]</code> will be added after the key to make it an array key
	 * @param paramValues the value array to add
	 * @see #addUrlParameter(StringBuilder, String, Object)
	 */
	protected void addUrlParameter(StringBuilder url, String paramKey, Object[] paramValues) {
		String arrayKey = paramKey + "[]";
		for (Object value : paramValues) {
			addUrlParameter(url, arrayKey, value);
		}
	}

	/**
	 * Get the specific url
	 *  
	 * @return the specific url, must NOT start with a slash '/'
	 */
	protected abstract String getSpecificUrl();

}
