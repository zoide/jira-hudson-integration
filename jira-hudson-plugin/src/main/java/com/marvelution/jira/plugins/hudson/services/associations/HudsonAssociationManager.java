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

package com.marvelution.jira.plugins.hudson.services.associations;

import java.util.Collection;

import com.atlassian.jira.project.Project;

/**
 * Hudson Association Manager Interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonAssociationManager {

	/**
	 * Check if there is a {@link HudsonAssociation} configured with the given Id
	 * 
	 * @param associationId the Id to check
	 * @return <code>true</code> if configured, <code>false</code> otherwise
	 */
	boolean hasAssociation(int associationId);

	/**
	 * Check if any {@link HudsonAssociation}s are configured
	 * 
	 * @return <code>true</code> if there are any, <code>false</code> otherwise
	 */
	boolean hasAssocations();

	/**
	 * Check if there are any {@link HudsonAssociation}s for the given {@link Project}
	 * 
	 * @param project the {@link Project} to check
	 * @return <code>true</code> if there are any associations, <code>false</code> otherwise
	 */
	boolean hasAssociations(Project project);

	/**
	 * Getter for a specific {@link HudsonAssociation} by its Id
	 * 
	 * @param associationId the {@link HudsonAssociation} Id to get
	 * @return the {@link HudsonAssociation}
	 */
	HudsonAssociation getAssociation(int associationId);

	/**
	 * Getter for all the {@link HudsonAssociation} objects in a unmodifiable {@link Collection}
	 * 
	 * @return the unmodifiable {@link Collection}
	 */
	Collection<HudsonAssociation> getAssociations();

	/**
	 * Get all the {@link HudsonAssociation} objects for the given {@link Project}
	 * 
	 * @param project the {@link Project} to get all the {@link HudsonAssociation} for
	 * @return the {@link Collection} with all {@link HudsonAssociation} associated with the {@link Project}
	 */
	Collection<HudsonAssociation> getAssociations(Project project);

	/**
	 * Add a {@link HudsonAssociation} to the collection
	 * 
	 * @param association the {@link HudsonAssociation} to all
	 */
	void addAssociation(HudsonAssociation association);

	/**
	 * Remove a {@link HudsonAssociation} from the collection by Id
	 * 
	 * @param associationId the {@link HudsonAssociation} Id to remove
	 * @see #removeAssociation(HudsonAssociation)
	 */
	void removeAssociation(int associationId);

	/**
	 * Remove a {@link HudsonAssociation} from the collection
	 * 
	 * @param association the {@link HudsonAssociation} to remove
	 */
	void removeAssociation(HudsonAssociation association);

}
