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

package com.marvelution.jira.plugins.hudson.streams;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.streams.api.common.Either;
import com.atlassian.streams.spi.StreamsCommentHandler;

/**
 * Hudson specific {@link StreamsCommentHandler} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public class HudsonStreamsCommentHandler implements StreamsCommentHandler {

	private final Logger logger = Logger.getLogger(HudsonStreamsCommentHandler.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Either<PostReplyError, URI> postReply(Iterable<String> itemPath, String comment) {
		logger.debug("Accessing postReply for " + StringUtils.join(itemPath.iterator(), ", "));
		return null;
	}

}
