Cascade-JRebelPlugins
=====================

JRebel Plugins for Cascade


1. Cascade Properties Reloader

When a pre-configured list of properties files is changed in the source directory, this plugin reloads the properties into the application.

JRebel automatically loads property files, however, frameworks such as Struts and Spring must be reinitialized with the file changes. The JRebel-Spring plugin detects changes in property files and reloads them, but the JRebel-Struts plugin does not. This plugin therefore detects property file changes and reloads the Struts ActionServlet when a change is detected.

