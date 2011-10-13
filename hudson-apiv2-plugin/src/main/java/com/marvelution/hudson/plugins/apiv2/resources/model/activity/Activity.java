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

package com.marvelution.hudson.plugins.apiv2.resources.model.activity;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.HudsonSystem;
import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.model.User;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Base Activity XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public abstract class Activity extends Model implements Comparable<Activity> {

	@XmlElement(name = "uri", required = true)
	private URI uri;
	@XmlElement(name = "type", required = true)
	private ActivityType type;
	@XmlElement(name = "title", required = true)
	private String title;
	@XmlElement(name = "system", required = true)
	private HudsonSystem system;
	@XmlElementRef
	private User user;
	@XmlElement(name = "timestamp", required = true)
	private long timestamp;

	
	/**
	 * Default Constructor
	 */
	public Activity() {}

	/**
	 * Constructor
	 * 
	 * @param type the {@link ActivityType}
	 */
	public Activity(ActivityType type) {
		this.type = type;
	}

	/**
	 * Getter for uri
	 *
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Setter for uri
	 *
	 * @param uri the uri to set
	 */
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * Getter for type
	 *
	 * @return the type
	 */
	public ActivityType getType() {
		return type;
	}

	/**
	 * Getter for title
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for title
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for system
	 *
	 * @return the system
	 */
	public HudsonSystem getSystem() {
		return system;
	}

	/**
	 * Setter for system
	 *
	 * @param system the system to set
	 */
	public void setSystem(HudsonSystem system) {
		this.system = system;
	}

	/**
	 * Getter for user
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter for user
	 *
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Getter for timestamp
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter for timestamp
	 *
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Activity other) {
		return Long.valueOf(getTimestamp()).compareTo(other.getTimestamp());
	}

	/**
	 * The {@link Job} specific {@link Activity} implementation
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 * @since 4.2.0
	 */
	@XmlType(name = "JobActivityType", namespace = NameSpaceUtils.ACTIVITY_NAMESPACE, factoryClass = ObjectFactory.class,
	factoryMethod = "createJobActivity")
	@XmlRootElement(name = "JobActivity", namespace = NameSpaceUtils.ACTIVITY_NAMESPACE)
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class JobActivity extends Activity {

		@XmlElementRef
		private Job job;

		/**
		 * Default Constructor
		 */
		public JobActivity() {
			super(ActivityType.JOB);
		}

		/**
		 * Getter for job
		 *
		 * @return the job
		 */
		public Job getJob() {
			return job;
		}

		/**
		 * Setter for job
		 *
		 * @param job the job to set
		 */
		public void setJob(Job job) {
			this.job = job;
			setTitle(job.getName());
		}

	}

	/**
	 * The {@link Build} specific {@link Activity} implementation
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 * @since 4.2.0
	 */
	@XmlType(name = "BuildActivityType", namespace = NameSpaceUtils.ACTIVITY_NAMESPACE, factoryClass = ObjectFactory.class,
	factoryMethod = "createBuildActivity")
	@XmlRootElement(name = "BuildActivity", namespace = NameSpaceUtils.ACTIVITY_NAMESPACE)
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class BuildActivity extends Activity {

		@XmlElementRef
		private Build build;

		/**
		 * Default Constructor
		 */
		public BuildActivity() {
			super(ActivityType.BUILD);
		}

		/**
		 * Getter for build
		 *
		 * @return the build
		 */
		public Build getBuild() {
			return build;
		}

		/**
		 * Setter for build
		 *
		 * @param build the build to set
		 */
		public void setBuild(Build build) {
			this.build = build;
			setTitle(String.format("%1$s #%2$d", build.getJobName(), build.getBuildNumber()));
		}

	}

}
