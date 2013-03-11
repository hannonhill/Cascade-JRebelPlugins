package com.hannonhill.cascade.jrebel.propreloader;

import org.zeroturnaround.javarebel.Logger;
import org.zeroturnaround.javarebel.StopWatch;


public class LogWrapper implements Logger {

	private static final String LOGGER_PREFIX = "JRebel-Cascade-Properties-Reloader: ";
	private Logger log;
	
	public LogWrapper(Logger log) {
		this.log = log;
	}

	@Override
	public StopWatch createStopWatch(String arg0) {
		return log.createStopWatch(arg0);
	}

	@Override
	public void echo() {
		log.echo(LOGGER_PREFIX);
	}

	@Override
	public void echo(String arg0) {
		log.echo(LOGGER_PREFIX.concat(arg0));
	}

	@Override
	public void echoPrefix(String arg0) {
		log.echoPrefix(LOGGER_PREFIX.concat(arg0));
	}

	@Override
	public void error(Throwable arg0) {
		log.error(arg0);
	}

	@Override
	public void errorEcho(Throwable arg0) {
		log.errorEcho(arg0);
	}

	@Override
	public void errorTrace(Throwable arg0) {
		log.errorTrace(arg0);
	}

	@Override
	public boolean isEnabled() {
		return log.isEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	@Override
	public void log(String arg0) {
		log.log(arg0);
	}

	@Override
	public Logger prefix(String arg0) {
		return log.prefix(arg0);
	}

	@Override
	public Logger productPrefix(String arg0) {
		return log.productPrefix(arg0);
	}

	@Override
	public void trace(String arg0) {
		log.trace(arg0);
	}
	
}
