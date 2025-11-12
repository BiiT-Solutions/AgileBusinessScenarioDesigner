package com.biit.abcd.logger;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Logger)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.biit.logger.BiitLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines basic log behavior with log4j.properties. The info, warning, debug
 * and severe methods are reimplemented to ensure the JVM to load this class and
 * not optimize with BiitLogger directly.
 */
public class AbcdLogger extends BiitLogger {
    private static final Logger logger = LoggerFactory.getLogger(AbcdLogger.class);

    /**
     * Events that have business meaning (i.e. creating category, deleting form,
     * ...). To follow user actions.
     *
     * @param className class to be logged.
     * @param message   text that will appear on the log.
     */
    public static void info(String className, String message) {
        info(logger, className, message);
    }

    public static void info(String className, String messageTemplate, Object... arguments) {
        info(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not
     * allowed for this user, ...
     *
     * @param className class to be logged.
     * @param message   text that will appear on the log.
     */
    public static void warning(String className, String message) {
        warning(logger, className, message);
    }

    public static void warning(String className, String messageTemplate, Object... arguments) {
        warning(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application
     * access to a method, opening database connection, etc.
     *
     * @param className class to be logged.
     * @param message   text that will appear on the log.
     */
    public static void debug(String className, String message) {
        debug(logger, className, message);
    }

    public static void debug(String className, String messageTemplate, Object... arguments) {
        debug(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * To log any not expected error that can cause application malfuncionality.
     *
     * @param className class to be logged.
     * @param message   text that will appear on the log.
     */
    public static void severe(String className, String message) {
        severe(logger, className, message);
    }

    /**
     * To log any not expected error that can cause application malfuncionality.
     *
     * @param className class to be logged.
     * @param throwable exception that will appear on the log.
     */
    public static void severe(String className, Throwable throwable) {
        String error = getStackTrace(throwable);
        severe(logger, className, error);
    }

    public static void severe(String className, String messageTemplate, Object... arguments) {
        severe(logger, className + ": " + messageTemplate, arguments);
    }

    /**
     * To log java exceptions and log also the stack trace. If enabled, also can
     * send an email to the administrator to alert of the error.
     *
     * @param className class to be logged.
     * @param throwable exception that will appear on the log.
     */
    public static void errorMessage(String className, Throwable throwable) {
        String error = getStackTrace(throwable);
        errorMessageNotification(logger, className, error);
    }

    public static void errorMessage(String className, String error) {
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
