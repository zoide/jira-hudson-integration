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

package com.marvelution.hudson.plugins.jirareporter.utils;

import java.io.StringWriter;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;

import com.marvelution.hudson.plugins.apiv2.dozer.utils.DozerUtils;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.TestResult;
import com.marvelution.hudson.plugins.jirareporter.JIRASite;

import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.util.LogTaskListener;

/**
 * Utility class for Issue Text generation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class IssueTextUtils {

	private static final Logger LOGGER = Logger.getLogger(IssueTextUtils.class.getName());

	/**
	 * Create the text content for a specific issue field
	 * 
	 * @param type the {@link Type} of text to create
	 * @param build the {@link AbstractBuild}
	 * @param site the {@link JIRASite}
	 * @return the created text content
	 */
	public static String createFieldText(Type type, AbstractBuild<?, ?> build, JIRASite site) {
		final StringWriter writer = new StringWriter();
		final XMLOutput output = XMLOutput.createXMLOutput(writer);
		final JellyContext context = new JellyContext();
		context.setVariable("rootURL", Hudson.getInstance().getRootUrl());
		context.setVariable("build", build);
		context.setVariable("site", site);
		try {
			Class.forName("jenkins.model.Jenkins");
			context.setVariable("system", "Jenkins");
		} catch (ClassNotFoundException e) {
			context.setVariable("system", "Hudson");
		}
		context.setVariable("version", Hudson.getVersion().toString());
		try {
			context.setVariable("environment", build.getEnvironment(new LogTaskListener(LOGGER, Level.INFO)));
		} catch (Exception e) {
			context.setVariable("environment", Collections.emptyMap());
		}
		// Utilize the Dozer Mapper and its convertors of the API V2 plugin to get the ChangeLog and TestResults
		try {
			context.setVariable("changelog", DozerUtils.getMapper().map(build.getChangeSet(), ChangeLog.class));
		} catch (Exception e) {
			context.setVariable("changelog", new ChangeLog());
		}
		try {
			context.setVariable("testresults", DozerUtils.getMapper().map(build, TestResult.class));
		} catch (Exception e) {
			context.setVariable("testresults", new TestResult());
		}
		try {
			context.runScript(HudsonPluginUtils.getPluginClassloader().getResource("fields/" + type.field(site)
				+ ".jelly"), output);
		} catch (JellyException e) {
			LOGGER.log(Level.SEVERE, "Failed to create Text of type " + type.name(), e);
			throw new IllegalStateException("Cannot raise an issue if no text is available", e);
		}
		return writer.toString().trim();
	}

	/**
	 * Text Type enumeration
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static enum Type {

		SUMMARY, DESCRIPTION, ENVIRONMENT;

		/**
		 * Getter for the field name
		 * 
		 * @param site the {@link JIRASite} object the text will be send to, used to determine in the Wiki template
		 * 			should be used
		 * @return the field name
		 */
		String field(JIRASite site) {
			if (site.supportsWikiStyle) {
				return name().toLowerCase() + "-wiki";
			} else {
				return name().toLowerCase();
			}
		}

	}

}
