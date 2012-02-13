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

package com.marvelution.jira.plugins.hudson.servlets;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.connectors.ConnectorResponse;
import com.marvelution.hudson.plugins.apiv2.client.connectors.HttpClient4Connector;
import com.marvelution.jira.plugins.hudson.servlets.query.QueryWrapper;

/**
 * {@link HttpServlet} to handle secured requests to Hudson or Jenkins
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * 
 * @since 4.2.0
 */
public class HudsonMakeRequestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final Logger logger = Logger.getLogger(HudsonMakeRequestServlet.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUrl = req.getParameter("url");
		String requestType = req.getParameter("type");
		if (StringUtils.isNotBlank(requestUrl)) {
			logger.debug("Got a makeRequest request for " + requestUrl);
			try {
				URI uri = new URI(requestUrl);
				HudsonClient hudson = new HudsonClient(new HttpClient4Connector(getHost(uri)));
				QueryWrapper query = new QueryWrapper(uri);
				if (StringUtils.isBlank(requestType) || "json".equalsIgnoreCase(requestType)) {
					query.setAcceptHeader(MediaType.APPLICATION_JSON);
					resp.setContentType(MediaType.APPLICATION_JSON);
				} else {
					query.setAcceptHeader(MediaType.APPLICATION_XML);
					resp.setContentType(MediaType.APPLICATION_XML);
				}
				ConnectorResponse response = hudson.getConnector().execute(query);
				resp.setCharacterEncoding("UTF-8");
				IOUtils.copy(response.getResponseAsReader(), resp.getWriter());
				resp.getWriter().flush();
			} catch (URISyntaxException e) {
			}
		}
	}

	/**
	 * Get the {@link Host} from a given {@link URI}
	 * 
	 * @param uri the {@link URI}
	 * @return the {@link Host}
	 */
	private Host getHost(URI uri) {
		StringBuilder hostUri = new StringBuilder();
		hostUri.append(uri.getScheme()).append("://");
		if (uri.getAuthority().indexOf("@") > -1) {
			hostUri.append(uri.getAuthority().substring(uri.getAuthority().lastIndexOf("@") + 1));
		} else {
			hostUri.append(uri.getAuthority());
		}
		Host host = new Host(hostUri.toString());
		if (uri.getAuthority().indexOf("@") > -1) {
			String userInfo = uri.getAuthority().substring(0, uri.getAuthority().lastIndexOf("@"));
			host.setUsername(userInfo.substring(0, userInfo.indexOf(":")));
			host.setPassword(userInfo.substring(userInfo.indexOf(":") + 1));
		}
		return host;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
