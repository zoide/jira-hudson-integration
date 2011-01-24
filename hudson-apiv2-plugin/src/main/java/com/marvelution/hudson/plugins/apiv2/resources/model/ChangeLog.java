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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Change Log XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlType(name = "ChangeLogType", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlRootElement(name = "ChangeLog", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeLog extends ListableModel<ChangeLog.Entry> {

	@XmlElementRef
	private Collection<Entry> entries;

	/**
	 * Default Constructor
	 */
	public ChangeLog() {
		entries = new ArrayList<Entry>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Entry> getItems() {
		return entries;
	}

	/**
	 * Change Log Entry XML Object
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlType(name = "EntryType", namespace = NameSpaceUtils.BUILD_NAMESPACE)
	@XmlRootElement(name = "Entry", namespace = NameSpaceUtils.BUILD_NAMESPACE)
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Entry extends Model {

		@XmlElement(name = "user", required = true)
		private String user;
		@XmlElement(name = "message", required = true)
		private String message;

		/**
		 * Default Constructor
		 */
		public Entry() {
		}

		/**
		 * Constructor
		 * 
		 * @param user the user that created the log
		 * @param message the message that was added in the log
		 */
		public Entry(String user, String message) {
			this.user = user;
			this.message = message;
		}

		/**
		 * Getter for user
		 * 
		 * @return the user
		 */
		public String getUser() {
			return user;
		}

		/**
		 * Setter for user
		 * 
		 * @param user the user to set
		 */
		public void setUser(String user) {
			this.user = user;
		}

		/**
		 * Getter for message
		 * 
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Setter for message
		 * 
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

	}

}
