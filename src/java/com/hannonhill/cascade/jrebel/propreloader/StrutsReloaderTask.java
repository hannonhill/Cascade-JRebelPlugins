package com.hannonhill.cascade.jrebel.propreloader;

import java.lang.reflect.Method;

import org.apache.struts.action.ActionServlet;
import org.zeroturnaround.javarebel.Logger;

public class StrutsReloaderTask implements Runnable {

	private ActionServlet actionServlet;
	private Logger log;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		StrutsInitMonitor.obtainLock();
		try {
			Class actionServletClass = ActionServlet.class;
			Method destroyModulesMethod = actionServletClass.getDeclaredMethod("destroyModules", new Class[0]);
			destroyModulesMethod.setAccessible(true);
			destroyModulesMethod.invoke(actionServlet, new Object[0]);
			
			actionServlet.init();
		} catch (Exception e) {
			log.echo("Could not reload Struts");
			log.error(e);
		} finally {
			StrutsInitMonitor.releaseLock();
		}
	}

	/**
	 * @return the actionServlet
	 */
	public ActionServlet getActionServlet() {
		return actionServlet;
	}

	/**
	 * @param actionServlet
	 *            the actionServlet to set
	 */
	public void setActionServlet(ActionServlet actionServlet) {
		this.actionServlet = actionServlet;
	}

	/**
	 * @return the log
	 */
	public Logger getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(Logger log) {
		this.log = log;
	}

}
