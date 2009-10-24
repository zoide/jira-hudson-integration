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

package com.marvelution.jira.plugins.hudson.gadgets.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Abstract Resource for Gadget responses
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlRootElement
public abstract class AbstractResource {

	@XmlElement
	private boolean hasErrors;

	@XmlElement
	private Collection<String> errors;

	/**
	 * Default Constructor
	 */
	public AbstractResource() {
	}

	/**
	 * Constructor
	 * 
	 * @param errors {@link Collection} of i18n error keys, may be <code>null</code>
	 */
	public AbstractResource(Collection<String> errors) {
		this.errors = errors;
		hasErrors = (this.errors != null ? !this.errors.isEmpty() : false);
	}

	/**
	 * @return the hasErrors
	 */
	public boolean isHasErrors() {
		return hasErrors;
	}

	/**
	 * @return the errors
	 */
	public Collection<String> getErrors() {
		return errors;
	}
}
