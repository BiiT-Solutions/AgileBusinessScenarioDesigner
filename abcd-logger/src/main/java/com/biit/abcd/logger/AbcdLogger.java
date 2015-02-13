package com.biit.abcd.logger;

import org.apache.log4j.Logger;

import com.biit.logger.BiitLogger;

/**
 * Defines basic log behavior with log4j.properties. The info, warning, debug and severe methods are reimplemented to
 * ensure the JVM to load this class and not optimize with BiitLogger directly.
 */
public class AbcdLogger extends BiitLogger {
	static {
		setLogger(Logger.getLogger(new Object() {
		}.getClass().getEnclosingClass()));
	}

	/**
	 * Events that have business meaning (i.e. creating category, deleting form, ...). To follow user actions.
	 */
	public static void info(String className, String message) {
		info(className + ": " + message);
	}

	/**
	 * Shows not critical errors. I.e. Email address not found, permissions not allowed for this user, ...
	 * 
	 * @param message
	 */
	public static void warning(String className, String message) {
		warning(className + ": " + message);
	}

	/**
	 * For following the trace of the execution. I.e. Knowing if the application access to a method, opening database
	 * connection, etc.
	 */
	public static void debug(String className, String message) {
		debug(className + ": " + message);
	}

	/**
	 * To log any not expected error that can cause application malfuncionality.
	 * 
	 * @param message
	 */
	public static void severe(String className, String message) {
		severe(className + ": " + message);
	}
}
