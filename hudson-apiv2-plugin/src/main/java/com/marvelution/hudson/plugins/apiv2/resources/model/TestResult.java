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

package com.marvelution.hudson.plugins.apiv2.resources.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * TestResult XML object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "TestResultType", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlRootElement(name = "TestResult", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class TestResult {

	@XmlElement(name = "failed", required = true)
	private int failed;
	@XmlElement(name = "skipped", required = true)
	private int skipped;
	@XmlElement(name = "total", required = true)
	private int total;
	@XmlElement(name = "failedTest")
	@XmlElementWrapper(name = "failedTests")
	private Collection<String> failedTests;

	/**
	 * Getter for the successful tests count
	 * 
	 * @return the count
	 */
	public int getSuccessful() {
		return total - skipped - failed;
	}

	/**
	 * Getter for the failed tests count
	 * 
	 * @return the count
	 */
	public int getFailed() {
		return failed;
	}

	/**
	 * Setter for the failed tests count
	 * 
	 * @param failed the count
	 */
	public void setFailed(int failed) {
		this.failed = failed;
	}

	/**
	 * Getter for the failed tests
	 * 
	 * @return the {@link Collection} of failed tests
	 */
	public Collection<String> getFailedTests() {
		if (failedTests == null) {
			failedTests = new ArrayList<String>();
		}
		return failedTests;
	}

	/**
	 * Getter for the skipped count
	 * 
	 * @return the skipped count
	 */
	public int getSkipped() {
		return skipped;
	}

	/**
	 * Setter for the skipped count
	 * 
	 * @param skipped the skipped count
	 */
	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	/**
	 * Getter for the total count
	 * 
	 * @return the total count
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Setter for the total count
	 * 
	 * @param total the total count
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Getter for the total passed count
	 * 
	 * @return the passed count
	 */
	public int getPassed() {
		return total - skipped - failed;
	}

}
