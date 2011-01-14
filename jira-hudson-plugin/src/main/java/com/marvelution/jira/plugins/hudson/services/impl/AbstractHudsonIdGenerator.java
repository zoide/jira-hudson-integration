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

package com.marvelution.jira.plugins.hudson.services.impl;

import com.marvelution.jira.plugins.hudson.services.HudsonIdGenerator;
import com.marvelution.jira.plugins.hudson.services.HudsonPropertyManager;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 */
public abstract class AbstractHudsonIdGenerator implements HudsonIdGenerator {

	private final HudsonPropertyManager propertyManager;

	/**
	 * Constructor
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 */
	protected AbstractHudsonIdGenerator(HudsonPropertyManager propertyManager) {
		this.propertyManager = propertyManager;
	}

	/**
	 * Internal method to get the name of the Next ID Key
	 * 
	 * @return the next Id key
	 */
	protected abstract String getNextIdKey();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int next() {
		int nextId = 1;
		if (propertyManager.getPropertySet().exists(getNextIdKey())) {
			nextId = propertyManager.getPropertySet().getInt(getNextIdKey());
		}
		propertyManager.getPropertySet().setInt(getNextIdKey(), nextId + 1);
		return nextId;
	}

}
