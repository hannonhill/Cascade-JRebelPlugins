package com.hannonhill.cascade.jrebel.propreloader;

import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.zeroturnaround.bundled.javassist.ClassPool;
import org.zeroturnaround.bundled.javassist.CtClass;
import org.zeroturnaround.bundled.javassist.CtMethod;
import org.zeroturnaround.bundled.javassist.NotFoundException;
import org.zeroturnaround.javarebel.Logger;
import org.zeroturnaround.javarebel.LoggerFactory;
import org.zeroturnaround.javarebel.integration.support.JavassistClassBytecodeProcessor;


public class StrutsInitMonitor {
	
	private StrutsInitMonitor() {
	}
	
	private static final Lock monitorLock = new ReentrantLock();
	
	private static Long lastInitializeStart = 0L;
	private static Boolean initializing = false;
	
	public static void obtainLock() {
		monitorLock.lock();
	}
	
	public static void releaseLock() {
		monitorLock.unlock();
	}
	
	public static void beforeInitialized() {
		obtainLock();
		try {
			lastInitializeStart = Calendar.getInstance().getTimeInMillis();
			initializing = true;
		} finally {
			releaseLock();
		}
	}
	
	public static void afterInitialized(Object obj) {
		obtainLock();
		try {
			initializing = false;
		} finally {
			releaseLock();
		}
	}
	
	public static Long getLastInitializeStart() {
		return lastInitializeStart;
	}
	
	public static Boolean isInitializing() {
		return initializing;
	}
	
	public static class StrutsInitCBP extends JavassistClassBytecodeProcessor {

		@Override
		public void process(ClassPool clPool, ClassLoader clLoader, CtClass ctClass) throws Exception {
			Logger log = new LogWrapper(LoggerFactory.getInstance());
			
			log.echo("Patching the Struts ActionServlet ...");
			
			try {
				CtMethod paintMethod = ctClass.getDeclaredMethod("init");
				paintMethod.insertBefore("com.hannonhill.cascade.jrebel.propreloader.StrutsInitMonitor.beforeInitialized();");
				paintMethod.insertAfter("com.hannonhill.cascade.jrebel.propreloader.StrutsInitMonitor.afterInitialized($0);");

			} catch (NotFoundException e) {
				log.error(e);
			}
			
		}
		
	}
}
