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

package com.marvelution.jira.plugins.hudson.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XmlType(name = "ServerType", namespace = "http://jira.marvelution.com/hudson")
@XmlRootElement(name = "Server", namespace = "http://jira.marvelution.com/hudson")
@XmlAccessorType(XmlAccessType.FIELD)
public class Server {

	private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

	@XmlElement
	private int serverId;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private String host;

	/**
	 * Default Constructor
	 */
	public Server() {
	}

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer} to copy the data from
	 */
	public Server(HudsonServer server) {
		setServerId(server.getID());
		setName(server.getName());
		setDescription(server.getDescription());
		setHost(server.getPublicHost());
	}

	/**
	 * Getter for serverId
	 * 
	 * @return the serverId
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * Setter for serverId
	 * 
	 * @param serverId the serverId to set
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * Getter for name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for host
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Setter for host
	 * 
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(this, object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, Server.TO_STRING_STYLE);
	}

}
