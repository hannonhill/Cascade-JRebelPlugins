package com.hannonhill.cascade.jrebel.propreloader;

import java.io.File;

public class MonitoredFile {

	private File file;
	private Long lastModified;
	
	public MonitoredFile(File file) {
		this.file = file;
		lastModified = file.lastModified();
	}
	
	/**
	 * @return true if the file has been modified since the last time this method was invoked.
	 */
	public boolean updateModified() {
		if (file.lastModified() > lastModified) {
			lastModified = file.lastModified();
			return true;
		}
		
		return false;
	}
	
	public Long getLastModified() {
		return lastModified;
	}
	
	public String getFileName() {
		return file.getAbsolutePath();
	}
}
