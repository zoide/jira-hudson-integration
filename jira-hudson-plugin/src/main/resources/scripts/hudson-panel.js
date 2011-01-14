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

AJS.$.namespace("AJS.hudson.panel");

/**
 * Base object containing all the options for the panel
 */
AJS.hudson.panel.config = {};

AJS.hudson.panel.initializePanel = function() {
	AJS.$("#" + AJS.hudson.panel.config.viewsContainer + " ." + AJS.hudson.panel.config.tabClass).each(function(index, item) {
		var tab = AJS.$("#" + item.id);
		var tabData = tab.attr('id').split('_');
		tab.click(function(event) {
			AJS.hudson.panel.troggleView(tabData[1]);
			event.preventDefault();
		});
		if (AJS.hudson.panel.config.view == "" || AJS.hudson.panel.config.view == tabData[1]) {
			tab.addClass("selected");
			AJS.hudson.panel.config.view = tabData[1];
		}
	});
	AJS.$("#" + AJS.hudson.panel.config.associationsContainer + " ." + AJS.hudson.panel.config.tabClass).each(function(index, item) {
		var tab = AJS.$("#" + item.id);
		var tabData = tab.attr('id').split('_');
		tab.click(function(event) {
			AJS.hudson.panel.troggleAssociation(tabData[1]);
			event.preventDefault();
		});
		if (AJS.hudson.panel.config.associationId == 0 || AJS.hudson.panel.config.associationId == tabData[1]) {
			tab.addClass("selected");
			AJS.hudson.panel.config.associationId = tabData[1];
		}
	});
	AJS.hudson.panel.updatePanelContent();
}

AJS.hudson.panel.troggleView = function(view) {
	AJS.hudson.panel.config.view = view;
	AJS.$("#" + AJS.hudson.panel.config.viewsContainer + " ." + AJS.hudson.panel.config.tabClass).each(function(index, item) {
		AJS.$("#" + item.id).removeClass("selected");
	});
	AJS.$("#view_" + view).addClass("selected");
	AJS.hudson.panel.updatePanelContent();
}

AJS.hudson.panel.troggleAssociation = function(associationId) {
	AJS.hudson.panel.config.associationId = associationId;
	AJS.$("#" + AJS.hudson.panel.config.associationsContainer + " ." + AJS.hudson.panel.config.tabClass).each(function(index, item) {
		AJS.$("#" + item.id).removeClass("selected");
	});
	AJS.$("#association_" + associationId).addClass("selected");
	AJS.hudson.panel.updatePanelContent();
}

AJS.hudson.panel.updatePanelContent = function() {
	jQuery.ajax({
	    type : "GET",
	    dataType: "html",
	    data: ({
	    	view: AJS.hudson.panel.config.view,
	    	associationId: AJS.hudson.panel.config.associationId,
	    	module: AJS.hudson.panel.config.module,
	    	objectId: AJS.hudson.panel.config.objectId
	    }),
	    url : AJS.hudson.panel.config.contextPath + "/secure/ViewHudsonPanelContent.jspa",
	    success : function(html) {
	        jQuery('#hudsonTabPanelHtml').html(html);
	    }
	});
}
