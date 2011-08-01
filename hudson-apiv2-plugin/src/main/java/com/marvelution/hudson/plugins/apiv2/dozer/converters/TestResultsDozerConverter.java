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

package com.marvelution.hudson.plugins.apiv2.dozer.converters;

import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.TestResult;
import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;


/**
 * A {@link DozerConverter} to get the {@link TestResult} from an {@link hudson.model.AbstractBuild}
 * This converter also supports TestNG next to Surefire
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("rawtypes")
public class TestResultsDozerConverter extends DozerConverter<hudson.model.AbstractBuild, TestResult> implements MapperAware {

	private Mapper mapper;

	/**
	 * Constructor
	 */
	public TestResultsDozerConverter() {
		super(hudson.model.AbstractBuild.class, TestResult.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestResult convertTo(hudson.model.AbstractBuild source, TestResult destination) {
		if (source.getTestResultAction() != null) {
			// We have Surefire test results, map these to the TestResult object
			return mapper.map(source.getTestResultAction(), TestResult.class);
		} else if (HudsonPluginUtils.hasTestNGPlugin()) {
			// We have the TestNG plugin installed, maybe there are TestNG test results?
			hudson.plugins.testng.TestNGBuildAction buildAction = source.getAction(hudson.plugins.testng.TestNGBuildAction.class);
			if (buildAction != null) {
				return mapper.map(buildAction.getResults(), TestResult.class);
			}
		}
		// Return an empty TestResult
		return new TestResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public hudson.model.AbstractBuild convertFrom(TestResult source, hudson.model.AbstractBuild destination) {
		throw new UnsupportedOperationException("Cannot convert a TestResult into an hudson.model.AbstractBuild");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

}
