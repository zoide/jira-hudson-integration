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

package hudson.plugins.jiraapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;

import hudson.matrix.MatrixConfiguration;
import hudson.maven.MavenModule;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.ItemGroup;
import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;

/**
 * Helper class for Hudson Jobs
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ProjectUtils {

	/**
	 * Get all the Hudson Projects
	 * 
	 * @return {@link Set} of {@link AbstractProject} objects
	 */
	@SuppressWarnings("unchecked")
	public static Set<AbstractProject<?, ?>> getAllProjects() {
		final Set<AbstractProject<?, ?>> supported = new HashSet<AbstractProject<?, ?>>();
		final List<AbstractProject> projects = Hudson.getInstance().getAllItems(AbstractProject.class);
		for (AbstractProject<?, ?> project : projects) {
			if (isSupportedProjectType(project)) {
				supported.add(project);
			}
		}
		return supported;
	}

	/**
	 * Get all the Hudson Projects
	 * 
	 * @return {@link Set} of {@link AbstractProject} objects
	 */
	@SuppressWarnings("unchecked")
	public static Set<AbstractProject<?, ?>> getAllProjectsIncludingModules() {
		final Set<AbstractProject<?, ?>> supported = new HashSet<AbstractProject<?, ?>>();
		final List<AbstractProject> projects = Hudson.getInstance().getAllItems(AbstractProject.class);
		for (AbstractProject<?, ?> project : projects) {
			supported.add(project);
		}
		return supported;
	}

	/**
	 * Get the Hudson Project by Jira Project Key
	 * 
	 * @param key the Jira project key
	 * @return the {@link AbstractProject}, may be <code>null</code> if no {@link AbstractProject} can be found
	 */
	public static Set<AbstractProject<?, ?>> getProjectByJiraProjectKey(final String key) {
		final Set<AbstractProject<?, ?>> jiraProjects = new HashSet<AbstractProject<?, ?>>();
		final Set<AbstractProject<?, ?>> projects = getAllProjectsIncludingModules();
		for (AbstractProject<?, ?> project : projects) {
			if (project.getProperty(JiraProjectKeyJobProperty.class) != null) {
				final JiraProjectKeyJobProperty jiraProperty =
					(JiraProjectKeyJobProperty) project.getProperty(JiraProjectKeyJobProperty.class);
				if (key.equals(jiraProperty.getKey())) {
					if (isSupportedProjectType(project)) {
						jiraProjects.add(project);
					} else {
						jiraProjects.add((AbstractProject<?, ?>) project.getParent());
					}
				}
			}
		}
		return jiraProjects;
	}

	/**
	 * Get a Hudson project by name
	 * 
	 * @param projectName the name of the project
	 * @param parentName the name of the project parent, may be <code>null</code> if the project doesn't have a
	 *            parent, required if looking up a subproject like MavenModule
	 * @return the {@link AbstractProject} object
	 */
	public static AbstractProject<?, ?> getProjectByName(String projectName, String parentName) {
		if (StringUtils.isEmpty(parentName)) {
			return (AbstractProject<?, ?>) Hudson.getInstance().getItem(projectName);
		} else {
			final AbstractProject<?, ?> parent = (AbstractProject<?, ?>) Hudson.getInstance().getItem(parentName);
			return (AbstractProject<?, ?>) ((ItemGroup<?>) parent).getItem(projectName);
		}
	}

	/**
	 * Check if the Jira integration supports the project
	 * 
	 * @param <PROJECT> the Type of the project
	 * @param project the {@link AbstractProject} project to check
	 * @return <code>true</code> if supported, <code>false</code> otherwise
	 */
	public static <PROJECT extends AbstractProject<?, ?>> boolean isSupportedProjectType(PROJECT project) {
		return (!(project instanceof MavenModule) && !(project instanceof MatrixConfiguration));
	}

	/**
	 * Get all the builds of a project
	 * 
	 * @param <PROJECT> the Type of project
	 * @param project the project to get all the builds for
	 * @return {@link List} of Builds
	 */
	@SuppressWarnings("unchecked")
	public static <PROJECT extends AbstractProject<?, ?>> List<AbstractBuild<?, ?>> getProjectModuleBuilds(
					PROJECT project) {
		final List<AbstractBuild<?, ?>> builds = new ArrayList<AbstractBuild<?, ?>>();
		if (project instanceof ItemGroup) {
			final Collection<PROJECT> modules = ((ItemGroup<PROJECT>) project).getItems();
			for (PROJECT module : modules) {
				builds.addAll(module.getBuilds());
			}
		}
		return builds;
	}

	/**
	 * Get all the builds of a {@link List} of projects
	 * 
	 * @param <PROJECT> the Type of project
	 * @param projects the {@link List} of projects to get all the builds for
	 * @return {@link List} of Builds
	 */
	public static <PROJECT extends AbstractProject<?, ?>> List<AbstractBuild<?, ?>> getProjectModuleBuilds(
					Collection<PROJECT> projects) {
		final List<AbstractBuild<?, ?>> builds = new ArrayList<AbstractBuild<?, ?>>();
		for (PROJECT project : projects) {
			builds.addAll(getProjectModuleBuilds(project));
		}
		return builds;
	}

}
