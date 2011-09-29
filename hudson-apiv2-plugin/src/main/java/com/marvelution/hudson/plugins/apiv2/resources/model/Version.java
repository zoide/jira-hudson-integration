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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Version XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlType(name = "VersionType", namespace = NameSpaceUtils.APIV2_NAMESPACE)
@XmlRootElement(name = "Version", namespace = NameSpaceUtils.APIV2_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Version extends Model {

	@XmlElement(name = "hudsonSystem")
	private HudsonSystem hudsonSystem;
	@XmlElement(name = "hudsonVersion")
	private String hudsonVersion;
	@XmlElement(name = "pluginVersion")
	private String pluginVersion;
	@XmlElement(name = "pluginGroupId")
	private String pluginGroupId;
	@XmlElement(name = "pluginArtifactId")
	private String pluginArtifactId;

	/**
	 * Default constructor
	 */
	public Version() {}

	/**
	 * Constructor
	 * 
	 * @param hudsonSystem the {@link HudsonSystem}
	 * @param hudsonVersion the hudsonVersion
	 * @param pluginVersion the pluginVersion
	 * @param pluginGroupId the group Id
	 * @param artifactId the artifact Id
	 */
	public Version(HudsonSystem hudsonSystem, String hudsonVersion, String pluginVersion, String pluginGroupId,
			String pluginArtifactId) {
		this.hudsonSystem = hudsonSystem;
		this.hudsonVersion = hudsonVersion;
		this.pluginVersion = pluginVersion;
		this.pluginGroupId = pluginGroupId;
		this.pluginArtifactId = pluginArtifactId;
	}

	/**
	 * Getter for hudsonSystem
	 * 
	 * @return the hudsonSystem
	 * @since 4.1.0
	 */
	public HudsonSystem getHudsonSystem() {
		return hudsonSystem;
	}

	/**
	 * Setter for hudsonSystem
	 * 
	 * @param hudsonSystem the hudsonSystem to set
	 * @since 4.1.0
	 */
	public void setHudsonSystem(HudsonSystem hudsonSystem) {
		this.hudsonSystem = hudsonSystem;
	}

	/**
	 * Getter for hudsonVersion
	 * 
	 * @return the hudsonVersion
	 */
	public String getHudsonVersion() {
		return hudsonVersion;
	}

	/**
	 * Setter for hudsonVersion
	 * 
	 * @param hudsonVersion the hudsonVersion to set
	 */
	public void setHudsonVersion(String hudsonVersion) {
		this.hudsonVersion = hudsonVersion;
	}

	/**
	 * Getter for pluginVersion
	 * 
	 * @return the pluginVersion
	 */
	public String getPluginVersion() {
		return pluginVersion;
	}

	/**
	 * Setter for pluginVersion
	 * 
	 * @param pluginVersion the pluginVersion to set
	 */
	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	/**
	 * Getter for luginGroupId
	 * 
	 * @return the pluginGroupId
	 */
	public String getPluginGroupId() {
		return pluginGroupId;
	}

	/**
	 * Setter for pluginGroupId
	 * 
	 * @param pluginGroupId the pluginGroupId to set
	 */
	public void setPluginGroupId(String pluginGroupId) {
		this.pluginGroupId = pluginGroupId;
	}

	/**
	 * Getter for pluginArtifactId
	 * 
	 * @return the pluginArtifactId
	 */
	public String getPluginArtifactId() {
		return pluginArtifactId;
	}

	/**
	 * Setter for pluginArtifactId
	 * 
	 * @param pluginArtifactId the pluginArtifactId to set
	 */
	public void setPluginArtifactId(String pluginArtifactId) {
		this.pluginArtifactId = pluginArtifactId;
	}

}
