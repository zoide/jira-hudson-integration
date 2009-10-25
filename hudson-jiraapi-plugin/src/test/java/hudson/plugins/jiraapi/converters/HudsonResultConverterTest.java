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

package hudson.plugins.jiraapi.converters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import com.marvelution.jira.plugins.hudson.api.model.Result;

/**
 * Testcase for {@link HudsonResultConverter}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonResultConverterTest {

	/**
	 * Test {@link HudsonResultConverter#convertHudsonResult(hudson.model.Result)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testConvertSuccess() throws Exception {
		assertThat(HudsonResultConverter.convertHudsonResult(hudson.model.Result.SUCCESS), is(Result.SUCCESS));
	}

	/**
	 * Test {@link HudsonResultConverter#convertHudsonResult(hudson.model.Result)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testConvertFailure() throws Exception {
		assertThat(HudsonResultConverter.convertHudsonResult(hudson.model.Result.FAILURE), is(Result.FAILURE));
	}

	/**
	 * Test {@link HudsonResultConverter#convertHudsonResult(hudson.model.Result)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testConvertUnstable() throws Exception {
		assertThat(HudsonResultConverter.convertHudsonResult(hudson.model.Result.UNSTABLE), is(Result.UNSTABLE));
	}

	/**
	 * Test {@link HudsonResultConverter#convertHudsonResult(hudson.model.Result)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testConvertNotBuild() throws Exception {
		assertThat(HudsonResultConverter.convertHudsonResult(hudson.model.Result.NOT_BUILT), is(Result.NOT_BUILT));
	}

	/**
	 * Test {@link HudsonResultConverter#convertHudsonResult(hudson.model.Result)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testConvertAborted() throws Exception {
		assertThat(HudsonResultConverter.convertHudsonResult(hudson.model.Result.ABORTED), is(Result.ABORTED));
	}

	/**
	 * Test {@link HudsonResultConverter#convertHudsonResult(hudson.model.Result)}
	 * 
	 * @throws Exception in case of test errors
	 */
	@Test
	public void testConvert() throws Exception {
		assertThat(HudsonResultConverter.convertHudsonResult(null), is(Result.NOT_BUILT));
	}

}
