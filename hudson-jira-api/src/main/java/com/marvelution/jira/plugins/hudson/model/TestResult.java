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

package com.marvelution.jira.plugins.hudson.model;

import java.util.ArrayList;
import java.util.Collection;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Model class for test results
 * 
 * @author <a href="milto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("testResult")
public class TestResult {

	private int failed;

	private Collection<String> failedTests;

	private int skipped;

	private int total;

	/**
	 * Gets the successfull tests count
	 * 
	 * @return the count
	 */
	public int getSuccessful() {
		return total - skipped - failed;
	}

	/**
	 * Gets the failed tests count
	 * 
	 * @return the count
	 */
	public int getFailed() {
		return failed;
	}

	/**
	 * Sets the failed tests count
	 * 
	 * @param failed the count
	 */
	public void setFailed(int failed) {
		this.failed = failed;
	}

	/**
	 * Gets the failed tests
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
	 * Gets the skipped count
	 * 
	 * @return the skipped count
	 */
	public int getSkipped() {
		return skipped;
	}

	/**
	 * Sets the skipped count
	 * 
	 * @param skipped the skipped count
	 */
	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	/**
	 * Gets the total count
	 * 
	 * @return the total count
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Sets the total count
	 * 
	 * @param total the total count
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Gets the total passed count
	 * 
	 * @return the passed count
	 */
	public int getPassed() {
		return total - skipped - failed;
	}

}
