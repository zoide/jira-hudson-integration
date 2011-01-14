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

package com.marvelution.jira.plugins.hudson.panels.resultset;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * Result Set interface that is given to the Velocity template to generate the view
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface ResultSet<MODEL extends Model> {

	/**
	 * Getter for the Velocity View to render
	 * 
	 * @return the name of the velocity view
	 */
	ResultViews getView();

	/**
	 * Getter for the {@link HudsonServer}
	 * 
	 * @return the {@link HudsonServer}
	 */
	HudsonServer getServer();

	/**
	 * Getter for the results
	 * 
	 * @return the results <MODEL> object
	 */
	MODEL getResults();

	/**
	 * Result Set View enumeration
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static enum ResultViews {

		BUILDS, JOBSTATUS;

		/**
		 * Getter for the velocity view name
		 * 
		 * @return the velocity view name
		 */
		public String getView() {
			return name().toLowerCase();
		}

	}

}
