${project.name}
-------------------------------
${project.version}

What is it?
-------------------------------
${project.description}


Hudson CI - Quick Installation Guide
-------------------------------
1. Shutdown the Hudson server instance
2. Remove any existing Jira Hudson Integration plugins of previous versions
3a. Install the new plugin (hudson-apiv2-plugin-${project.version}.hpi) in the [HUDSON_HOME]/plugins directory
3b. Optionally install the new pugin (hudson-jira-reporter-${project.version}.hpi) in the [HUDSON_HOME]/plugins directory
4. Start the Hudson server instance

JIRA - Quick Installation Guide
-------------------------------
1. Shutdown the JIRA server instance
2. Remove any existing Jira Hudson Integration plugins of previous versions
3. Install the new plugin (jira-hudson-plugin-${prject.version}.jar) in the {JIRA_HOME]/plugins/installed-plugins directory
4. Start the JIRA server instance


MORE?
-------------------------------
Detailed installation and usage instructions?

    http://docs.marvelution.com/display/MARVJIRAHUDSON

Suggestions, bug reports or feature requests?

    ${project.issueManagement.url}

Support?

    Create a Support ticket at ${project.issueManagement.url}


Enjoy! :)
