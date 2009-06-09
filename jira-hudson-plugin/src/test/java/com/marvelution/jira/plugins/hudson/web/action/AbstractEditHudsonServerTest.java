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

package com.marvelution.jira.plugins.hudson.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.marvelution.jira.plugins.hudson.JiraApi;
import com.marvelution.jira.plugins.hudson.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;

/**
 * Abstract TestCase for all EditHudsonServer Tests
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractEditHudsonServerTest extends AbstractHudsonWebActionSupportTest {

	protected AbstractEditHudsonServer editHudsonServer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		editHudsonServer = (AbstractEditHudsonServer) webworkAction;
	}

	/**
	 * Test validation with a valid server name
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationName() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.setHost("http://hudson.marvelution.com");
		editHudsonServer.doValidation();
		assertFalse(webworkAction.getHasErrors());
	}

	/**
	 * Test validation with a blank server name
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationBlankName() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		editHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("name"));
		assertEquals("hudson.config.serverName.required", webworkAction.getErrors().get("name"));
	}

	/**
	 * Test validation with a blank server host
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationBlankHost() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("host"));
		assertEquals("hudson.config.host.required", webworkAction.getErrors().get("host"));
	}

	/**
	 * Test validation with an invalid server host (not starting with http(s)://
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationValidHost() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.setHost("http://hudson.marvelution.com");
		editHudsonServer.doValidation();
		assertFalse(webworkAction.getHasErrors());
	}

	/**
	 * Test validation with an invalid server host (not starting with http(s)://
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationInvalidHost() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.setHost("i.am.not.valid");
		editHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("host"));
		assertEquals("hudson.config.host.invalid", webworkAction.getErrors().get("host"));
	}

	/**
	 * Test validation of correct Api Implementation version of the configured server
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationCorrectApiImplementation() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.setHost("http://hudson.marvelution.com/");
		editHudsonServer.doValidation();
		assertFalse(webworkAction.getHasErrors());
	}

	/**
	 * Test validation of correct Api Implementation version of the configured server
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationIncorrectApiImplementation() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(new ApiImplementation());
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.setHost("http://hudson.marvelution.com/");
		editHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("host"));
		assertEquals("hudson.config.host.incompatible.api.version", webworkAction.getErrors().get("host"));
	}

	/**
	 * Test validation of correct Api Implementation version of the configured server
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public void testDoValidationServerAccessorException() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenThrow(
			new HudsonServerAccessorException("Failed to connect to Hudson", new Exception()));
		editHudsonServer.setName("Hudson CI");
		editHudsonServer.setHost("http://hudson.marvelution.com/");
		editHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("host"));
		assertEquals("hudson.config.host.connection.error", webworkAction.getErrors().get("host"));
	}

	/**
	 * Test the validation without errors
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public abstract void testDoValidationWithoutErrors() throws Exception;

	/**
	 * Test the validation with errors
	 * 
	 * @throws Exception in case of test exception
	 */
	@Test
	public abstract void testDoValidationWithErrors() throws Exception;

	/**
	 * Test doExecute without errors
	 * 
	 * @throws Exception in case of test failure
	 */
	@Test
	public abstract void testDoExecuteWithoutErros() throws Exception;

	/**
	 * Test doExecute with errors
	 * 
	 * @throws Exception in case of test failure
	 */
	@Test
	public abstract void testDoExecuteWithErros() throws Exception;

}
