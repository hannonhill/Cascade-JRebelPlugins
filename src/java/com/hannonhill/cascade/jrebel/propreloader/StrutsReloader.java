package com.hannonhill.cascade.jrebel.propreloader;

import org.apache.struts.action.ActionServlet;
import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.NotFoundException;
import org.zeroturnaround.javarebel.Logger;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;

public class StrutsReloader {

	private static final Logger LOG = new LogWrapper(LoggerFactory.getInstance());
	
	public static MonitoredFileManager monitoredFileManager = new MonitoredFileManager();
	
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
	
	public static class StrutsReloaderCBP extends JavassistClassBytecodeProcessor {

		@Override
		public void process(ClassPool clPool, ClassLoader clLoader, CtClass ctClass) throws Exception {
			Logger log = new LogWrapper(LoggerFactory.getInstance());
			log.echo("Patching struts ActionServlet reloading");
			
			try {
				CtMethod paintMethod = ctClass.getDeclaredMethod("process");
				paintMethod.insertBefore("com.hannonhill.cascade.jrebel.propreloader.StrutsReloader.preProcess();");
				
			} catch (NotFoundException e) {
				log.error(e);
			}
		}
		
	}
}
