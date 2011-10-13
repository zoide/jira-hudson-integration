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

import static com.atlassian.streams.api.ActivityObjectTypes.newTypeFactory;
import static com.atlassian.streams.api.ActivityVerbs.ATLASSIAN_IRI_BASE;
import static com.atlassian.streams.api.ActivityVerbs.newVerbFactory;
import static com.atlassian.streams.api.ActivityVerbs.update;
import static com.atlassian.streams.api.StreamsFilterType.SELECT;
import static com.atlassian.streams.api.StreamsFilterType.LIST;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.streams.api.ActivityObjectType;
import com.atlassian.streams.api.ActivityObjectTypes;
import com.atlassian.streams.api.ActivityVerb;
import com.atlassian.streams.api.ActivityVerbs;
import com.atlassian.streams.api.StreamsFilterType;
import com.atlassian.streams.spi.StandardStreamsFilterOption;
import com.atlassian.streams.spi.StreamsFilterOption;
import com.atlassian.streams.spi.StreamsFilterOptionProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;

/**
 * Hudson specific implementation of the {@link StreamsFilterOptionProvider}
 * This can provide filtering options that will be displayed to the user in the stream gadget configuration.
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public class HudsonStreamsFilterOptionProvider implements StreamsFilterOptionProvider {

    public static final String HUDSON_BASE_ACTIVITY_OBJECT_TYPE = "hudsonEvent";
	public static final String HUDSON_SERVER_KEY = "hudson-server";
	public static final String HUDSON_ASSOCIATION_KEY = "hudson-association";
	public static final String HUDSON_JOB_KEY = "hudson-job";

	private static final String HUDSON_IRI_BASE = ATLASSIAN_IRI_BASE + "hudson/";
	private static final ActivityVerbs.VerbFactory ACTIVITY_VERB_FACTORY = newVerbFactory(HUDSON_IRI_BASE);
    private static final ActivityObjectTypes.TypeFactory ACTIVITY_TYPE_FACTORY = newTypeFactory(HUDSON_IRI_BASE);
	public static final ActivityObjectType HUDSON_ACTIVITY_OBJECT_TYPE =
		ACTIVITY_TYPE_FACTORY.newType(HUDSON_BASE_ACTIVITY_OBJECT_TYPE);

	private final I18nResolver i18nResolver;
	private final HudsonAssociationManager associationManager;
	private final HudsonServerManager serverManager;

	/**
	 * Constructor
	 *
	 * @param i18nResolver the {@link I18nResolver} implementation
	 */
	public HudsonStreamsFilterOptionProvider(I18nResolver i18nResolver, HudsonAssociationManager associationManager,
				HudsonServerManager serverManager) {
		this.i18nResolver = checkNotNull(i18nResolver, "i18nResolver");
		this.associationManager = checkNotNull(associationManager, "associationManager");
		this.serverManager = checkNotNull(serverManager, "serverManager");
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<StreamsFilterOption> getFilterOptions() {
		ImmutableList.Builder<StreamsFilterOption> options = ImmutableList.builder();
		options.add(StandardStreamsFilterOption.USER);
		options.add(getStreamsFilterOption(HUDSON_SERVER_KEY, SELECT, "streams.filter.option.hudson.server",
			"Server", true, "streams.filter.option.hudson.server.help", getServerOptionsMap()));
		options.add(getStreamsFilterOption(HUDSON_ASSOCIATION_KEY, SELECT, "streams.filter.option.hudson.association",
			"Association", true, "streams.filter.option.hudson.association.help", getAssociationOptionsMap()));
		options.add(getStreamsFilterOption(HUDSON_JOB_KEY, LIST, "streams.filter.option.hudson.job",
			"Job", true, "streams.filter.option.hudson.job.help", null));
		return options.build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ActivityOption> getActivities() {
		ImmutableList.Builder<ActivityOption> options = ImmutableList.builder();
		for (ActivityType activity : ActivityType.values()) {
			options.add(new ActivityOption(i18nResolver.getText(getI18nKeyForActivity(activity)),
				HUDSON_ACTIVITY_OBJECT_TYPE, getActivityVerbForActivity(activity)));
		}
		return options.build();
	}

	/**
	 * Getter for a {@link ActivityVerb} from a {@link ActivityType}
	 * 
	 * @param option the {@link ActivityType} to get the {@link ActivityVerb} from
	 * @return the {@link ActivityVerb}
	 */
	public static ActivityVerb getActivityVerbForActivity(ActivityType activity) {
		return ACTIVITY_VERB_FACTORY.newVerb(activity.name().toLowerCase(), update());
	}

	/**
	 * Get the I18N key for the given {@link ActivityType}
	 * 
	 * @param activity the {@link ActivityType} to get he I18N key for
	 * @return the I18N key
	 */
	private String getI18nKeyForActivity(ActivityType activity) {
		return String.format("streams.hudson.activity.option.%1$s", activity.name().toLowerCase().replace('_', '.'));
	}

	/**
	 * Helper method to get a {@link StreamsFilterOption}
	 * 
	 * @param key the key
	 * @param type the {@link StreamsFilterType}
	 * @param i18nKey the i18n key
	 * @param displayName the display name
	 * @param unique flag if the value needs to be unique
	 * @param helpTextI18nKey help i18n key
	 * @param values {@link Map} of possible values
	 * @return the {@link StreamsFilterOption}
	 */
	private StreamsFilterOption getStreamsFilterOption(String key, StreamsFilterType type, String i18nKey,
			String displayName, boolean unique, String helpTextI18nKey, Map<String, String> values) {
		StreamsFilterOption.Builder builder = new StreamsFilterOption.Builder(key, type).displayName(displayName).i18nKey(i18nKey)
			.helpTextI18nKey(helpTextI18nKey).unique(unique);
		if (values != null) {
			builder.values(values);
		}
		return builder.build();
	}

	/**
	 * Getter for the available Servers map
	 * 
	 * @return the {@link Map} with server Id as key and name as value
	 */
	private Map<String, String> getServerOptionsMap() {
		ImmutableMap.Builder<String, String> options = ImmutableMap.builder();
		for (HudsonServer server : serverManager.getServers()) {
			options.put(String.valueOf(server.getServerId()), server.getName());
		}
		return options.build();
	}

	/**
	 * Getter for the available Associations map
	 * 
	 * @return the {@link Map} with association Id as key and server name with jobname as value
	 */
	private Map<String, String> getAssociationOptionsMap() {
		ImmutableMap.Builder<String, String> options = ImmutableMap.builder();
		for (HudsonAssociation association : associationManager.getAssociations()) {
			HudsonServer server = serverManager.getServer(association.getServerId());
			options.put(String.valueOf(association.getAssociationId()), server.getName() + " / "
				+ association.getJobName());
		}
		return options.build();
	}

}
