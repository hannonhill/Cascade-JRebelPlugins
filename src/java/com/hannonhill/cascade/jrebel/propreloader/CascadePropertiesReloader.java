package com.hannonhill.cascade.jrebel.propreloader;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.struts.action.ActionServlet;
import org.zeroturnaround.javarebel.ClassResourceSource;
import org.zeroturnaround.javarebel.Integration;
import org.zeroturnaround.javarebel.IntegrationFactory;
import org.zeroturnaround.javarebel.Logger;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.Plugin;

import com.hannonhill.cascade.jrebel.propreloader.StrutsInitMonitor.StrutsInitCBP;

public class CascadePropertiesReloader implements Plugin {

	private static final Logger LOG = new LogWrapper(LoggerFactory.getInstance());	

	private static final String CLASSES_ROOT = System.getProperty("cascade.proj") + "/bin";
	
	/*
	 * Explicit list of property files this plugin monitors (classpath only)
	 */
	private static final String[] monitoredFilePaths = new String[] {
		"/com/hannonhill/cascade/resources/application.properties",
		"/com/hannonhill/cascade/resources/application_ar.properties",
		"/com/hannonhill/cascade/resources/application_de.properties",
		"/com/hannonhill/cascade/resources/application_es.properties",
		"/com/hannonhill/cascade/resources/application_fr.properties",
		"/com/hannonhill/cascade/resources/application_it.properties",
		"/com/hannonhill/cascade/resources/application_ja.properties",
		"/com/hannonhill/cascade/resources/application_zh.properties"
	};

	private static final MonitoredFileManager monitoredFileManager = new MonitoredFileManager();
	
	public synchronized static void preProcess(ActionServlet actionServlet) {
		StrutsInitMonitor.obtainLock();
		try {
			if (StrutsInitMonitor.isInitializing()) {
				return;
			}
			
			if (monitoredFileManager.fileChanged() && monitoredFileManager.getLastModified() > StrutsInitMonitor.getLastInitializeStart()) {
				reinitializeActionServlet(actionServlet);
			}
		} finally {
			StrutsInitMonitor.releaseLock();
		}
		
	}
	
	private static void reinitializeActionServlet(ActionServlet actionServlet) {
		StrutsReloaderTask reloadTask = new StrutsReloaderTask();
		reloadTask.setActionServlet(actionServlet);
		reloadTask.setLog(LOG);
		
		new Thread(reloadTask).start();
	}
	
	@Override
	public boolean checkDependencies(ClassLoader classLoader, ClassResourceSource classResourceSource) {
		boolean result = classResourceSource.getClassResource("org.apache.struts.action.ActionServlet") != null;
		if (!result) {
			LoggerFactory.getInstance().echo("Dependency org.apache.struts.action.ActionServlet not found");
		}
		
		return result;
	}

	@Override
	public synchronized void preinit() {
	    
		try {
			initMonitoredFiles();
		} catch (URISyntaxException e) {
			LoggerFactory.getInstance().errorEcho(e);
			return;
		}
		
		// Register class bytecode processor to monitor the last initialized time of the Struts action servlet
		Integration integration = IntegrationFactory.getInstance();
		ClassLoader classLoader = CascadePropertiesReloader.class.getClassLoader();
		
	    integration.addIntegrationProcessor(classLoader, "org.apache.struts.action.ActionServlet", new StrutsInitCBP());
	}
	
	private void initMonitoredFiles() throws URISyntaxException {
		for (String path : monitoredFilePaths) {
			File file = new File(CLASSES_ROOT.concat(path));
			
			monitoredFileManager.addFileFromClasspath(file);
			LoggerFactory.getInstance().echo("Monitoring " + file.getName() + " for changes");
		}
	}
	
	@Override
	public String getAuthor() {
		return "John Lazos\nSoftware Engineer\nHannon Hill Corporation, Atlanta, GA";
	}

	@Override
	public String getDescription() {
		return "Monitors a running Cascade instance for changes to properties files. Changes trigger JRebel plugins for frameworks such as Struts and Spring to reload with the updated properties.";
	}

	@Override
	public String getId() {
		return "cascade-properties-reloader";
	}

	@Override
	public String getName() {
		return "JRebel Cascade Properties Reloader";
	}

	@Override
	public String getSupportedVersions() {
		return null;
	}

	@Override
	public String getTestedVersions() {
		return null;
	}

	@Override
	public String getWebsite() {
		return "http://www.hannonhill.com";
	}
}
