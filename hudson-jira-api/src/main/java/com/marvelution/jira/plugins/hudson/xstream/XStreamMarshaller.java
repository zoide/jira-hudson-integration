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
import com.marvelution.jira.plugins.hudson.model.Builds;
import com.marvelution.jira.plugins.hudson.model.HealthReport;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.Jobs;
import com.marvelution.jira.plugins.hudson.model.TestResult;
import com.marvelution.jira.plugins.hudson.model.Version;
import com.marvelution.jira.plugins.hudson.xstream.converters.ResultConverter;
import com.marvelution.jira.plugins.hudson.xstream.converters.StateConverter;
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
	 */
	public static <T> String marshal(T source) {
		if (source == null) {
			return "";
		}
		boolean autoDetect = false;
		try {
			XStream.class.getMethod("autodetectAnnotations", boolean.class);
			autoDetect = true;
		} catch (Exception e) {
			LOGGER.debug("Cannot use Automatic Annotation Detecting XStream. Reason: " + e.getMessage(), e);
			autoDetect = false;
		}
		XStream xstream;
		if (autoDetect) {
			xstream = getAnnotationDetectingXStream();
		} else {
			xstream = getDefaultXStream();
		}
		return xstream.toXML(source);
	}

	/**
	 * Unmarshal the given XML {@link String} to an Object of Type T
	 * 
	 * @param <T> the Class Type of the unmarshaled XML Source
	 * @param xml the XML {@link String} source to unmarshal
	 * @param clazz the Class of the expected return Object
	 * @return the Unmarshaled Object
	 */
	public static <T> T unmarshal(String xml, Class<T> clazz) {
		if ("".equals(xml)) {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				return null;
			}
		}
		boolean autoDetect = false;
		try {
			XStream.class.getMethod("autodetectAnnotations", boolean.class);
			autoDetect = true;
		} catch (Exception e) {
			LOGGER.debug("Cannot use Automatic Annotation Detecting XStream. Reason: " + e.getMessage(), e);
			autoDetect = false;
		}
		XStream xstream;
		if (autoDetect) {
			xstream = getAnnotationDetectingXStream();
		} else {
			xstream = getDefaultXStream();
		}
		return clazz.cast(xstream.fromXML(xml));
	}

	/**
	 * Gets the Automatic Annotation Detecting {@link XStream} object
	 * 
	 * @return the automatic annotation detecting {@link XStream} 
	 */
	public static XStream getAnnotationDetectingXStream() {
		final XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(Version.class);
		xstream.processAnnotations(Jobs.class);
		xstream.processAnnotations(Job.class);
		xstream.processAnnotations(Builds.class);
		xstream.processAnnotations(Build.class);
		xstream.processAnnotations(HealthReport.class);
		xstream.processAnnotations(TestResult.class);
		return xstream;
	}

	/**
	 * Gets the default {@link XStream} object
	 * 
	 * @return default {@link XStream} object
	 */
	public static XStream getDefaultXStream() {
		final XStream xstream = new XStream();
		// Map all Jobs class element
		xstream.autodetectAnnotations(false);
		xstream.alias("apiVersion", Version.class);
		xstream.alias("jobs", Jobs.class);
		xstream.addImplicitCollection(Jobs.class, "jobs");
		// Map all Job class element
		xstream.alias("job", Job.class);
		xstream.omitField(Job.class, "hudsonServerId");
		xstream.aliasField("result", Job.class, "result");
		xstream.addImplicitCollection(Job.class, "healthReport");
		// Map all Builds class element
		xstream.alias("builds", Builds.class);
		xstream.addImplicitCollection(Builds.class, "builds");
		// Map all Build class element
		xstream.alias("build", Build.class);
		xstream.addImplicitCollection(Build.class, "trigger");
		xstream.addImplicitCollection(Build.class, "relatedIssueKey");
		xstream.omitField(Build.class, "hudsonServerId");
		xstream.aliasField("result", Build.class, "result");
		xstream.aliasField("state", Build.class, "state");
		// Map all HealthReport class element
		xstream.alias("healthReport", HealthReport.class);
		// Map all TestResult class element
		xstream.alias("testResult", TestResult.class);
		// Register Converters
		xstream.registerConverter(new ResultConverter());
		xstream.registerConverter(new StateConverter());
		return xstream;
	}

}
