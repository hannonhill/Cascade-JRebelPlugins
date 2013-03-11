Cascade-JRebelPlugins
=====================

JRebel Plugins for Cascade

========================================================
==  CONTENTS
========================================================

    I.   BUILDING
    II.  INSTALLING
    III. PLUGIN DOCS
         1.   Cascade Properties Reloader


========================================================
==  I.   BUILDING
========================================================

A build.xml is avaiable in the root project directory. The default target compiles all Cascade JRebel Plugins into separate JAR files in the dist directory.

========================================================
==  II.  INSTALLING
========================================================

Add the following to your Cascade server instance's JVM arguments:

 -Drebel.plugins=[/path/to/project/root]/dist/[name-of-plugin-jar].jar 
 -Drebel.[id-of-cascade-jrebel-plugin]=true

See III. Plugin Docs for plugin-specific JAR name, id, and configuration instructions

========================================================
==  III. PLUGIN DOCS
========================================================

Configuration instructions of each custom Cascade JRebel plugin

--------------------------------------------------------
1. Cascade Properties Reloader

   Id:  cascade-properties-reloader
   JAR: cscd-jrplugin-propreloader.jar

When a pre-configured list of properties files is changed in the source directory, this plugin reloads the properties into the application.

JRebel automatically loads property files, however, frameworks such as Struts and Spring must be reinitialized with the file changes. The JRebel-Spring plugin detects changes in property files and reloads them, but the JRebel-Struts plugin does not. This plugin therefore detects property file changes and reloads the Struts ActionServlet when a change is detected.

