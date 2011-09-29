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

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.ListableModel;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Change Log XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlType(name = "ChangeLogType", namespace = NameSpaceUtils.BUILD_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createChangeLog")
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
	@XmlType(name = "EntryType", namespace = NameSpaceUtils.BUILD_NAMESPACE, factoryClass = ObjectFactory.class,
			factoryMethod = "createChangeLogEntry")
	@XmlRootElement(name = "Entry", namespace = NameSpaceUtils.BUILD_NAMESPACE)
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Entry extends Model {

		@XmlElement(name = "user", required = true)
		private String user;
		@XmlElement(name = "message", required = true)
		private String message;
		@XmlElement(name = "affectedFile", required = true)
		@XmlElementWrapper(name = "affectedFiles", required = false)
		private Collection<AffectedFile> affectedFiles;
		

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

		/**
		 * Getter for affectedFiles
		 * 
		 * @return the affectedFiles
		 */
		public Collection<AffectedFile> getAffectedFiles() {
			if (affectedFiles == null) {
				return new ArrayList<ChangeLog.AffectedFile>();
			}
			return affectedFiles;
		}

		/**
		 * Setter for affectedFiles
		 * 
		 * @param affectedFiles the affectedFiles to set
		 */
		public void setAffectedFiles(Collection<AffectedFile> affectedFiles) {
			this.affectedFiles = affectedFiles;
		}

	}

	/**
	 * Change Log Affected File XML Object
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlType(name = "AffectedFileType", namespace = NameSpaceUtils.BUILD_NAMESPACE, factoryClass = ObjectFactory.class,
			factoryMethod = "createChangeLogAffectedFile")
	@XmlRootElement(name = "AffectedFile", namespace = NameSpaceUtils.BUILD_NAMESPACE)
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class AffectedFile extends Model {

		@XmlElement(name = "file", required = true)
		private String file;
		@XmlElement(name = "type", required = true, defaultValue = "UNKNOWN")
		private ChangeType type;

		/**
		 * Default Constructor
		 */
		public AffectedFile() {
		}

		/**
		 * Constructor
		 * 
		 * @param file the name of the changed file
		 * @param type the {@link ChangeType}
		 */
		public AffectedFile(String file, ChangeType type) {
			this.file = file;
			this.type = type;
		}

		/**
		 * Getter for file
		 * 
		 * @return the file
		 */
		public String getFile() {
			return file;
		}

		/**
		 * Setter for file
		 * 
		 * @param file the file to set
		 */
		public void setFile(String file) {
			this.file = file;
		}

		/**
		 * Getter for type
		 * 
		 * @return the type
		 */
		public ChangeType getType() {
			return type;
		}

		/**
		 * Setter for type
		 * 
		 * @param type the type to set
		 */
		public void setType(ChangeType type) {
			this.type = type;
		}

	}

	/**
	 * The ChangeType for {@link AffectedFile} elements
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlType(name = "ChangeType", namespace = NameSpaceUtils.BUILD_NAMESPACE)
	@XmlEnum(String.class)
	public static enum ChangeType {

		ADD, EDIT, DELETE, UNKNOWN;

	}

}
