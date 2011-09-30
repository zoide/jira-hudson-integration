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

package org.jvnet.hudson.test.recipes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.recipes.Recipe.Runner;

/**
 * Custom Annotation to inject Hudson configuration files
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * 
 * @since 4.2.0
 */
@Documented
@Recipe(TestData.RunnerImpl.class)
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestData {

	/**
	 * The data set in inject
	 * 
	 * @return the {@link DataSet}
	 */
	DataSet value();

	/**
	 * The {@link Runner} implementation that is executed to setup the testcase
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	class RunnerImpl extends Recipe.Runner<TestData> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setup(HudsonTestCase testCase, TestData recipe) {
			testCase.withPresetData(recipe.value().name().toLowerCase(Locale.ENGLISH).replace('_', '-'));
		}

	}

	/**
	 * Enumeration of the different Hudson testdata zip files that are available
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	enum DataSet {
		ANONYMOUS_NO_ACCESS, ANONYMOUS_READ_ONLY, ANONYMOUS_FULL_CONTROL,
		ANONYMOUS_NO_ACCESS_WITH_JOBS, ANONYMOUS_READ_ONLY_WITH_JOBS, ANONYMOUS_FULL_CONTROL_WITH_JOBS;
	}

}
