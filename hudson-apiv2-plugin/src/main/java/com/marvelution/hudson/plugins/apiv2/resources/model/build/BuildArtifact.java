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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.Model;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * BuildArtifact XML Object
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "BuildArtifactType", namespace = NameSpaceUtils.BUILD_NAMESPACE, factoryClass = ObjectFactory.class,
		factoryMethod = "createBuildArtifact")
@XmlRootElement(name = "BuildArtifact", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildArtifact extends Model {

	public static final String ARTIFACT_URL_PREFIX = "artifact/";

	@XmlElement(name = "name", required = true)
	private String name;
	@XmlElement(name = "url", required = true)
	private String url;

	/**
	 * Getter for the Name
	 * 
	 * @return the Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the Name
	 * 
	 * @param name the Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for the URL
	 * 
	 * @return the URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter for the URL
	 * 
	 * @param url the URL
	 */
	public void setUrl(String url) {
		if (!url.startsWith(ARTIFACT_URL_PREFIX)) {
			this.url = ARTIFACT_URL_PREFIX + url;
		} else {
			this.url = url;
		}
	}

}