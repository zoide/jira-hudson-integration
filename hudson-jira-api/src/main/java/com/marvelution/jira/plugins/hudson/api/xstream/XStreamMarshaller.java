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

package com.marvelution.jira.plugins.hudson.api.xstream;

import org.apache.log4j.Logger;

import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.BuildArtifact;
import com.marvelution.jira.plugins.hudson.api.model.BuildsList;
import com.marvelution.jira.plugins.hudson.api.model.HealthReport;
import com.marvelution.jira.plugins.hudson.api.model.HudsonView;
import com.marvelution.jira.plugins.hudson.api.model.HudsonViewsList;
import com.marvelution.jira.plugins.hudson.api.model.Job;
import com.marvelution.jira.plugins.hudson.api.model.JobsList;
import com.marvelution.jira.plugins.hudson.api.model.TestResult;
import com.marvelution.jira.plugins.hudson.api.model.triggers.ProjectTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.RemoteTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.SCMTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.TimeTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.Trigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.UserTrigger;
import com.marvelution.jira.plugins.hudson.api.xstream.converters.ResultConverter;
import com.marvelution.jira.plugins.hudson.api.xstream.converters.StateConverter;
import com.thoughtworks.xstream.XStream;

/**
 * Helper class to marshal Objects to and from XML usng {@link XStream}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class XStreamMarshaller {

	private static final Logger LOGGER = Logger.getLogger(XStreamMarshaller.class);

	private static XStream xstream = null;

	/**
	 * Marshal the given Source object to XML
	 * 
	 * @param <T> Class Type of the Source object to marshal
	 * @param source the source object to marshal
	 * @return the marshaled source object as {@link String}
	 * @throws XStreamMarshallerException in case the {@link XStream} object cannot be initialized
	 */
	public static <T> String marshal(T source) throws XStreamMarshallerException {
		if (source == null) {
			return "";
		}
		try {
			return getXStreamObject().toXML(source);
		} catch (Exception e) {
			LOGGER.debug("Cannot use Automatic Annotation Detecting XStream. Reason: " + e.getMessage(), e);
			throw new XStreamMarshallerException("Cannot get XStream object for marshalling", e);
		}
	}

	/**
	 * Unmarshal the given XML {@link String} to an Object of Type T
	 * 
	 * @param <T> the Class Type of the unmarshaled XML Source
	 * @param xml the XML {@link String} source to unmarshal
	 * @param clazz the Class of the expected return Object
	 * @return the Unmarshaled Object, may be <code>null</code>
	 * @throws XStreamMarshallerException in case the {@link XStream} object cannot be initialized
	 */
	public static <T> T unmarshal(String xml, Class<T> clazz) throws XStreamMarshallerException {
		if ("".equals(xml)) {
			return null;
		}
		try {
			return clazz.cast(getXStreamObject().fromXML(xml));
		} catch (Exception e) {
			LOGGER.debug("Cannot use Automatic Annotation Detecting XStream. Reason: " + e.getMessage(), e);
			throw new XStreamMarshallerException("Cannot get XStream object for unmarshalling", e);
		}
	}

	/**
	 * Get the XStream object, if the private variable xstream is <code>null</code> a new XStream marshaller object is
	 * created.
	 * 
	 * @return {@link XStream} object
	 */
	private static XStream getXStreamObject() {
		if (xstream != null) {
			return xstream;
		}
		xstream = new XStream();
		try {
			xstream.autodetectAnnotations(true);
			xstream.processAnnotations(ApiImplementation.class);
			xstream.processAnnotations(JobsList.class);
			xstream.processAnnotations(Job.class);
			xstream.processAnnotations(BuildsList.class);
			xstream.processAnnotations(Build.class);
			xstream.processAnnotations(HealthReport.class);
			xstream.processAnnotations(TestResult.class);
			xstream.processAnnotations(UserTrigger.class);
			xstream.processAnnotations(RemoteTrigger.class);
			xstream.processAnnotations(ProjectTrigger.class);
			xstream.processAnnotations(SCMTrigger.class);
			xstream.processAnnotations(TimeTrigger.class);
			xstream.processAnnotations(HudsonView.class);
			xstream.processAnnotations(HudsonViewsList.class);
			xstream.processAnnotations(BuildArtifact.class);
		} catch (Exception e) {
			xstream.alias("jiraApi", ApiImplementation.class);
			xstream.omitField(ApiImplementation.class, "api");
			xstream.omitField(ApiImplementation.class, "compatibleVersions");
			xstream.alias("jobs", JobsList.class);
			xstream.aliasField("job", JobsList.class, "jobs");
			xstream.alias("job", Job.class);
			xstream.omitField(Job.class, "hudsonServerId");
			xstream.addImplicitCollection(Job.class, "healthReports", HealthReport.class);
			xstream.registerConverter(new ResultConverter());
			xstream.registerConverter(new StateConverter());
			xstream.aliasField("result", Job.class, "result");
			xstream.aliasField("modules", Job.class, "modules");
			xstream.alias("builds", BuildsList.class);
			xstream.addImplicitCollection(BuildsList.class, "builds", Build.class);
			xstream.alias("build", Build.class);
			xstream.omitField(Build.class, "hudsonServerId");
			xstream.addImplicitCollection(Build.class, "triggers", Trigger.class);
			xstream.addImplicitCollection(Build.class, "relatedIssueKeys", String.class);
			xstream.addImplicitCollection(Build.class, "artifacts", BuildArtifact.class);
			xstream.aliasField("testResult", Build.class, "testResult");
			xstream.aliasField("result", Build.class, "result");
			xstream.aliasField("state", Build.class, "state");
			xstream.alias("healthReport", HealthReport.class);
			xstream.alias("testResult", TestResult.class);
			xstream.alias("userTrigger", UserTrigger.class);
			xstream.alias("remoteTrigger", RemoteTrigger.class);
			xstream.alias("projectTrigger", ProjectTrigger.class);
			xstream.alias("scmTrigger", SCMTrigger.class);
			xstream.alias("timeTrigger", TimeTrigger.class);
			xstream.alias("view", HudsonView.class);
			xstream.aliasField("jobs", HudsonView.class, "jobs");
			xstream.alias("views", HudsonViewsList.class);
			xstream.addImplicitCollection(HudsonView.class, "views", HudsonView.class);
			xstream.alias("artifact", BuildArtifact.class);
		}
		return xstream;
	}

}
