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

package com.marvelution.hudson.plugins.apiv2.resources.impl;

import java.util.logging.Logger;

import hudson.model.Hudson;

import javax.ws.rs.Path;

import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Scope;
import org.apache.wink.common.annotations.Scope.ScopeType;

import com.marvelution.hudson.plugins.apiv2.resources.PluginResource;
import com.marvelution.hudson.plugins.apiv2.resources.exceptions.ForbiddenException;
import com.marvelution.hudson.plugins.apiv2.resources.model.HudsonSystem;
import com.marvelution.hudson.plugins.apiv2.resources.model.Version;
import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

/**
 * The {@link PluginResource} REST implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Scope(ScopeType.SINGLETON)
@Parent(BaseRestResource.class)
@Path("plugin")
public class PluginRestResourceImpl extends BaseRestResource implements PluginResource {

	private final Logger log = Logger.getLogger(PluginRestResourceImpl.class.getName());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Version getVersion() {
		// At-least READ access is required to view this
		if (!Hudson.getInstance().hasPermission(Hudson.READ)) {
			throw new ForbiddenException();
		}
		HudsonSystem system = HudsonPluginUtils.getHudsonSystem();
		log.fine("Request for Plugin information returned: " + system.name() + ", " + Hudson.getVersion().toString());
		return new Version(system, Hudson.getVersion().toString(), HudsonPluginUtils.getPluginVersion(),
				HudsonPluginUtils.getPluginGroupId(), HudsonPluginUtils.getPluginArifactId());
	}

}
