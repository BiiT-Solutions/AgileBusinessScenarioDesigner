package com.biit.abcd.configuration;

import java.io.IOException;
import java.util.Properties;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import com.biit.utils.file.PropertiesFile;

public class AbcdConfigurationReader {
	private final String DATABASE_CONFIG_FILE = "settings.conf";
	// Regex Tags
	private final String NUMBER_REGEX_TAG = "numberRegEx";
	private final String DATE_REGEX_TAG = "dateRegEx";
	private final String POSTALCODE_REGEX_TAG = "postalCodeRegEx";
	// Prompts Tags
	private static final String ISSUE_MANAGER_URL = "issueManagerUrl";

	// Defaults
	private final String DEFAULT_NUMBER_CODE_REGEX = "[-]?[0-9]*\\.?,?[0-9]+";
	private final String DEFAULT_DATE_CODE_REGEX = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
	private final String DEFAULT_POSTAL_CODE_REGEX = "[0-9]{4}[a-zA-Z]{2}";

	private final String PLUGINS_PATH_TAG = "pluginsPath";
	private final String DEFAULT_PLUGINS_PATH = "plugins/";

	private static final String DEFAULT_ISSUE_MANAGER_URL = null;

	private String numberMask;
	private String dateMask;
	private String postalCodeMask;

	private String pluginsPath;

	private String issueManagerUrl;

	private static AbcdConfigurationReader instance;

	private AbcdConfigurationReader() {
		readConfig();
	}

	@FindBugsSuppressWarnings("DC_DOUBLECHECK")
	public static AbcdConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (AbcdConfigurationReader.class) {
				if (instance == null) {
					instance = new AbcdConfigurationReader();
				}
			}
		}
		return instance;
	}

	/**
	 * Read database config from resource and update default connection parameters.
	 */
	private void readConfig() {
		Properties prop = new Properties();
		try {
			prop = PropertiesFile.load(DATABASE_CONFIG_FILE);
			// Set masks
			numberMask = prop.getProperty(NUMBER_REGEX_TAG);
			dateMask = prop.getProperty(DATE_REGEX_TAG);
			postalCodeMask = prop.getProperty(POSTALCODE_REGEX_TAG);
			// Get plugins path
			pluginsPath = prop.getProperty(PLUGINS_PATH_TAG);
			issueManagerUrl = prop.getProperty(ISSUE_MANAGER_URL);
		} catch (IOException e) {
			// Do nothing.
		}
		checkForNullValues();
	}

	private void checkForNullValues() {
		if (numberMask == null) {
			numberMask = DEFAULT_NUMBER_CODE_REGEX;
		}
		if (dateMask == null) {
			dateMask = DEFAULT_DATE_CODE_REGEX;
		}
		if (postalCodeMask == null) {
			postalCodeMask = DEFAULT_POSTAL_CODE_REGEX;
		}
		if (pluginsPath == null) {
			pluginsPath = DEFAULT_PLUGINS_PATH;
		}

		if (issueManagerUrl == null) {
			issueManagerUrl = DEFAULT_ISSUE_MANAGER_URL;
		}
	}

	public String getNumberMask() {
		return numberMask;
	}

	public String getDateMask() {
		return dateMask;
	}

	public String getPostalCodeMask() {
		return postalCodeMask;
	}

	public String getPluginsPath() {
		return pluginsPath;
	}

	public String getIssueManagerUrl() {
		return issueManagerUrl;
	}
}
