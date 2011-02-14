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

import com.marvelution.hudson.plugins.apiv2.resources.model.view.View;
import com.marvelution.hudson.plugins.apiv2.resources.model.view.Views;

/**
 * {@link Query} implementation for {@link Job} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ViewQuery extends AbstractListableQuery<View, Views> {

	private String viewName = null;

	/**
	 * Private constructor to force the use of the static method below
	 */
	private ViewQuery() {
		super(View.class, Views.class);
	}

	/**
	 * Private constructor to force the use of the static method below
	 * 
	 * @param viewName name of a view on Hudson
	 */
	private ViewQuery(String viewName) {
		this();
		this.viewName = viewName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		final StringBuilder url = new StringBuilder();
		url.append("views");
		if (viewName != null) {
			url.append("?name=").append(viewName);
		} else {
			url.append("/all");
		}
		return url.toString();
	}

	/**
	 * Method to create a {@link ViewQuery} to get a specific {@link View}
	 * 
	 * @param viewName the name of the {@link View} to get
	 * @return the {@link ViewQuery}
	 */
	public static ViewQuery createForSpecificView(String viewName) {
		return new ViewQuery(viewName);
	}

	/**
	 * Method to create a {@link ViewQuery} to get all the Views on the Hudson server
	 * 
	 * @return the {@link ViewQuery}
	 */
	public static ViewQuery createForViewsList() {
		return new ViewQuery();
	}

}
