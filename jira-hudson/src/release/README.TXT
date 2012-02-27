${project.name}
-------------------------------
${project.version}

What is it?
-------------------------------
${project.description}


Hudson/Jenkins CI - Quick Installation Guide
--------------------------------------------
1. Shutdown the Hudson server instance
2. Remove any existing JIRA Hudson Integration plugins of previous versions
3a. Install the new plugin (hudson-apiv2-plugin-${project.version}.hpi) in the [HUDSON_HOME]/plugins directory
3b. Optionally install the new pugin (hudson-jira-reporter-${project.version}.hpi) in the [HUDSON_HOME]/plugins directory
4. Start the Hudson server instance

JIRA - Quick Installation Guide
-------------------------------
1. Logon to JIRA and navigate to Administration > Plugins
2. Using the Universal Plugin Manager:
2a. Update the plugin using the version on the Plugin Exchange (https://plugins.atlassian.com)
2b. Update the plugin by uploading the new version (jira-hudson-plugin-${project.version}.jar)
3. Don't forget to 'Drop Access' to the Administration section if you don't require it anymore


MORE?
-------------------------------
Detailed installation and usage instructions?

    http://docs.marvelution.com/display/MARVJIRAHUDSON

Suggestions, bug reports or feature requests?

    ${project.issueManagement.url}

Support?

    Create a Support ticket at ${project.issueManagement.url}


Enjoy! :)
