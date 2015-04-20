package com.biit.abcd.logger;

import org.apache.log4j.Logger;

import com.biit.logger.BiitLogger;

/**
 * Defines basic log behavior with log4j.properties. The info, warning, debug
 * and severe methods are reimplemented to ensure the JVM to load this class and
 * not optimize with BiitLogger directly.
 */
public class AbcdLogger extends BiitLogger {
	private static Logger logger = Logger.getLogger(AbcdLogger.class);
	
	/**
	 * Events that have business meaning (i.e. creating category, deleting form,
	 * ...). To follow user actions.
	 */
	public static void info(String className, String message) {
		info(logger, className, message);
	}

	/**
	 * Shows not critical errors. I.e. Email address not found, permissions not
	 * allowed for this user, ...
	 * 
	 * @param message
	 */
	public static void warning(String className, String message) {
		warning(logger, className, message);
	}

	/**
	 * For following the trace of the execution. I.e. Knowing if the application
	 * access to a method, opening database connection, etc.
	 */
	public static void debug(String className, String message) {
		debug(logger, className, message);
	}

	/**
	 * To log any not expected error that can cause application malfuncionality.
	 * 
	 * @param message
	 */
	public static void severe(String className, String message) {
		severe(logger, className, message);
	}

	/**
	 * To log any not expected error that can cause application malfuncionality.
	 * 
	 * @param message
	 */
	public static void severe(String className, Throwable throwable) {
		String error = getStackTrace(throwable);
		severe(logger, className, error);
	}

	/**
	 * To log java exceptions and log also the stack trace. If enabled, also can
	 * send an email to the administrator to alert of the error.
	 * 
	 * @param className
	 * @param throwable
	 */
	public static void errorMessage(String className, Throwable throwable) {
		String error = getStackTrace(throwable);
		errorMessageNotification(logger, className, error);
	}

	public static void timeLog(long millis, String method, String args) {
		if (logger.isDebugEnabled()) {
			StringBuilder logMessage = new StringBuilder();
			logMessage.append("Executed ");
			logMessage.append(method);
			logMessage.append("(");
			if (args != null && args.length() > 0) {
				logMessage.append(args);
			}
			logMessage.append(") in ");
			logMessage.append(millis);
			logMessage.append(" ms");

			logger.debug(logMessage.toString());
		}
	}
}
