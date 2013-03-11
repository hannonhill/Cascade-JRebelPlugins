package com.hannonhill.cascade.jrebel.propreloader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.zeroturnaround.javarebel.Logger;
import org.zeroturnaround.javarebel.LoggerFactory;

public class MonitoredFileManager {

	private static final Logger LOG = new LogWrapper(LoggerFactory.getInstance());
	
	private List<MonitoredFile> monitoredFiles = new ArrayList<MonitoredFile>();
	private Long lastModified = 0L;
	
	/**
	 * @param path The path of the file to load, absolute relative to classpath root 
	 * @throws URISyntaxException Unable to create URI from the specified path
	 */
	public void addFileFromClasspath(File file) {
		monitoredFiles.add(new MonitoredFile(file));
	}
	
	/**
	 * @return True if a monitored file has changed since the last time this method was invoked
	 */
	public boolean fileChanged() {
		boolean fileChanged = false;
		for (MonitoredFile file : monitoredFiles) {
			fileChanged = fileChanged || file.updateModified();
			if (fileChanged) {
				LOG.echo("File " + file.getFileName() + " modification detected");
			}
			if (fileChanged && lastModified < file.getLastModified()) {
				lastModified = file.getLastModified();
			}
		}
		
		return fileChanged;
	}
	
	public Long getLastModified() {
		return lastModified;
	}
}
