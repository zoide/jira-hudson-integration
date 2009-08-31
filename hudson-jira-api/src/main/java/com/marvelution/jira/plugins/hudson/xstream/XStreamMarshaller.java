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

package com.marvelution.jira.plugins.hudson.xstream;

import org.apache.log4j.Logger;

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.BuildArtifact;
import com.marvelution.jira.plugins.hudson.model.BuildsList;
import com.marvelution.jira.plugins.hudson.model.HealthReport;
import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.model.HudsonViewsList;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.JobsList;
import com.marvelution.jira.plugins.hudson.model.TestResult;
import com.marvelution.jira.plugins.hudson.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.model.triggers.LegacyCodeTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.ProjectTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.RemoteTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.SCMTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.TimeTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.UserTrigger;
import com.thoughtworks.xstream.XStream;

/**
 * Helper class to marshal Objects to and from XML usng {@link XStream}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class XStreamMarshaller {

	private static final Logger LOGGER = Logger.getLogger(XStreamMarshaller.class);

	/**
	 * Marshal the given Source object to XML
	 * 
	 * @param <T> Class Type of the Source object to marshal
	 * @param source the source object to marshal
	 * @return the marshaled source object as {@link String}
	 * @throws XStreamMarshallerException in case the {@link XStream} object cannot be initialised
	 */
	public static <T> String marshal(T source) throws XStreamMarshallerException {
		if (source == null) {
			return "";
		}
		try {
			XStream.class.getMethod("autodetectAnnotations", boolean.class);
			final XStream xstream = getAnnotationDetectingXStream();
			return xstream.toXML(source);
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
	 * @throws XStreamMarshallerException in case the {@link XStream} object cannot be initialised
	 */
	public static <T> T unmarshal(String xml, Class<T> clazz) throws XStreamMarshallerException {
		if ("".equals(xml)) {
			return null;
		}
		try {
			XStream.class.getMethod("autodetectAnnotations", boolean.class);
			final XStream xstream = getAnnotationDetectingXStream();
			return clazz.cast(xstream.fromXML(xml));
		} catch (Exception e) {
			LOGGER.debug("Cannot use Automatic Annotation Detecting XStream. Reason: " + e.getMessage(), e);
			throw new XStreamMarshallerException("Cannot get XStream object for marshalling", e);
		}
	}

	/**
	 * Gets the Automatic Annotation Detecting {@link XStream} object
	 * 
	 * @return the automatic annotation detecting {@link XStream} 
	 */
	public static XStream getAnnotationDetectingXStream() {
		final XStream xstream = new XStream();
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
		xstream.processAnnotations(LegacyCodeTrigger.class);
		xstream.processAnnotations(SCMTrigger.class);
		xstream.processAnnotations(TimeTrigger.class);
		xstream.processAnnotations(HudsonView.class);
		xstream.processAnnotations(HudsonViewsList.class);
		xstream.processAnnotations(BuildArtifact.class);
		return xstream;
	}

}
