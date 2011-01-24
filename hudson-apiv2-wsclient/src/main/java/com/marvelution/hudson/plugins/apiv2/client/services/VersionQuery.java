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

import com.marvelution.hudson.plugins.apiv2.resources.model.Version;

/**
 * {@link AbstractQuery} implementation for the {@link Version} endpoint
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class VersionQuery extends AbstractQuery<Version> {

	/**
	 * Constructor
	 */
	protected VersionQuery() {
		super(Version.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		final StringBuilder builder = new StringBuilder(super.getUrl());
		builder.append("/plugin").append("/version");
		return builder.toString();
	}

	/**
	 * Create a {@link VersionQuery} to get the plugin version
	 * 
	 * @return the {@link VersionQuery}
	 */
	public static VersionQuery createForPluginVersion() {
		return new VersionQuery();
	}

}
