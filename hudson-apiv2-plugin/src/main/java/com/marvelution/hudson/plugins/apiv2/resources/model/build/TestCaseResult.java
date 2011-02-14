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

package com.marvelution.hudson.plugins.apiv2.resources.model.build;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * TestCaseResult XML object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "TestCaseResultType", namespace = NameSpaceUtils.BUILD_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createTestCaseResult")
@XmlRootElement(name = "TestCaseResult", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class TestCaseResult extends Model {

	@XmlElement(name = "duration")
	private float duration;
	@XmlElement(name = "class", required = true)
	private String className;
	@XmlElement(name = "test", required = true)
	private String testName;
	@XmlElement(name = "skipped", required = true, defaultValue = "false")
	private boolean skipped;
	@XmlElement(name = "stackTrace")
	private String errorStackTrace;
	@XmlElement(name = "details")
	private String errorDetails;

	/**
	 * Getter for the duration
	 * 
	 * @return the duration
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * Setter for the duration
	 * 
	 * @param duration the duration to set
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	/**
	 * Getter for the className
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Setter for the className
	 * 
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Getter for the testName
	 * 
	 * @return the testName
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * Setter for the testName
	 * 
	 * @param testName the testName to set
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * Getter for the skipped flag
	 * 
	 * @return the skipped
	 */
	public boolean isSkipped() {
		return skipped;
	}

	/**
	 * Setter for the skipped flag
	 * 
	 * @param skipped the skipped to set
	 */
	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	/**
	 * Getter for the erroStackTrace
	 * 
	 * @return the errorStackTrace
	 */
	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	/**
	 * Setter for the errorStackTrace
	 * 
	 * @param errorStackTrace the errorStackTrace to set
	 */
	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}

	/**
	 * Getter for the erroDetails
	 * 
	 * @return the errorDetails
	 */
	public String getErrorDetails() {
		return errorDetails;
	}

	/**
	 * Setter for the errorDetails
	 * 
	 * @param errorDetails the errorDetails to set
	 */
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

}
