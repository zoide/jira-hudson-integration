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

AJS.$.namespace("AJS.gadget.fields");

/**
 * Create a Hudson Server picker
 * 
 * @param gadget the Gadget for which the picker must be made
 * @param userpref the name of the user preference where the serverId will be stored
 * @param options the JSON object with the servers
 * @return JSON object with the Gadget field configuration 
 */
AJS.gadget.fields.hudsonServerPicker = function(gadget, userpref, options) {
	if (!options) {
		options = {};
	}
	if (!options.servers) {
		options.servers = [];
	}
	if (!options.servers.length || options.servers.length === 0) {
		options.servers = [{label: gadget.getMsg("hudson.gadget.error.not.configured"), value: "-1"}];
	}
	return {
		userpref: userpref,
		label: gadget.getMsg("hudson.gadget.common.server.label"),
		description: gadget.getMsg("hudson.gadget.common.server.description"),
		type: "select",
		selected: gadget.getPref(userpref),
		options: options.servers
	};
};

/**
 * Create a Hudson Server picker
 * 
 * @param gadget the Gadget for which the picker must be made
 * @param userprefs the names of the user preference where the serverId will be stored
 * @param options the JSON object with the servers and views
 * @return JSON object with the Gadget field configuration 
 */
AJS.gadget.fields.hudsonServerViewPicker = function(gadget, userprefs, options) {
	if (!options) {
		options = {};
	}
	if (!options.views) {
		options.views = [];
	}
	if (!options.views.length || options.views.length === 0) {
		options.views = [{label: gadget.getMsg("hudson.gadget.error.not.configured"), value: "-1"}];
	}
	return {
		userpref: userprefs.server,
		label: gadget.getMsg("hudson.gadget.common.server.view.label"),
		description: gadget.getMsg("hudson.gadget.common.server.view.description"),
		id: "server_view_picker_" + userprefs.server,
		type: "callbackBuilder",
		callback: function(parentDiv) {
			parentDiv.append(
                AJS.$("<select/>").attr({
                	name: userprefs.server
                }).addClass("select-one")
            ).append(
                AJS.$("<input/>").attr({
                	id: "server_view_picker_" + userprefs.view,
                	name: userprefs.view,
                	type: "hidden",
                	value: gadget.getPref(userprefs.view)
                })
            );
			var selectList = AJS.$("#server_view_picker_" + userprefs.server + " select");
			var hiddenPref = AJS.$("#server_view_picker_" + userprefs.view);
			selectList.empty();
			AJS.$(options.views).each(function() {
				if (this.options) {
					var optGroup = AJS.$("<optgroup/>").attr({label: this.label});
					AJS.$(this.options).each(function() {
						optGroup.append(AJS.$("<option/>").attr({
							value: this.value,
							selected: (this.value == gadget.getPref(userprefs.server) && this.label == gadget.getPref(userprefs.view))
						}).text(this.label));
					});
					selectList.append(optGroup);
				} else {
					selectList.append(AJS.$("<option/>").attr({
						value: this.value,
						selected: (this.value == gadget.getPref(userprefs.server) && this.label == gadget.getPref(userprefs.view))
					}).text(this.label));
				}
			});
			selectList.change(function() {
				hiddenPref.val(selectList[0].options[selectList[0].selectedIndex].text);
			});
		}
	};
}
