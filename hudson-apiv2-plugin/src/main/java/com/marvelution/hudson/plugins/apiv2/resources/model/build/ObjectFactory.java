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

import javax.xml.bind.annotation.XmlRegistry;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog.AffectedFile;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog.Entry;

/**
 * ObjectFactory to create Xml objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Default Constructor
	 */
	public ObjectFactory() {
	}

	/**
	 * Create a {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public static Build createBuild() {
		return new Build();
	}

	/**
	 * Create a {@link Builds}
	 * 
	 * @return the {@link Builds}
	 */
	public static Builds createBuilds() {
		return new Builds();
	}

	/**
	 * Create a {@link BuildArtifact}
	 * 
	 * @return the {@link BuildArtifact}
	 */
	public static BuildArtifact createBuildArtifact() {
		return new BuildArtifact();
	}

	/**
	 * Create a {@link ChangeLog}
	 * 
	 * @return the {@link ChangeLog}
	 */
	public static ChangeLog createChangeLog() {
		return new ChangeLog();
	}

	/**
	 * Create a {@link Entry}
	 * 
	 * @return the {@link Entry}
	 */
	public static Entry createChangeLogEntry() {
		return new Entry();
	}

	/**
	 * Create a {@link AffectedFile}
	 * 
	 * @return the {@link AffectedFile}
	 */
	public static AffectedFile createChangeLogAffectedFile() {
		return new AffectedFile();
	}

	/**
	 * Create a {@link TestCaseResult}
	 * 
	 * @return the {@link TestCaseResult}
	 */
	public static TestCaseResult createTestCaseResult() {
		return new TestCaseResult();
	}

	/**
	 * Create a {@link TestResult}
	 * 
	 * @return the {@link TestResult}
	 */
	public static TestResult createTestResult() {
		return new TestResult();
	}

}
