package com.biit.abcd.configuration;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

public class AbcdConfigurationReader extends ConfigurationReader {

	private static final String DATABASE_CONFIG_FILE = "settings.conf";
	private static final String ABCD_SYSTEM_VARIABLE_CONFIG = "ABCD_CONFIG";

	// Regex Tags
	private static final String ID_NUMBER_REGEX = "numberRegEx";
	private static final String ID_DATE_REGEX = "dateRegEx";
	private static final String ID_POSTAL_REGEX = "postalCodeRegEx";
	// Prompts Tags
	private static final String ID_ISSUE_MANAGER_URL = "issueManagerUrl";

	// Defaults
	private static final String DEFAULT_NUMBER_REGEX = "[-]?[0-9]*\\.?,?[0-9]+";
	private static final String DEFAULT_DATE_REGEX = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
	private static final String DEFAULT_POSTAL_REGEX = "[0-9]{4}[a-zA-Z]{2}";
	private static final String DEFAULT_ISSUE_MANAGER_URL = null;

	private static AbcdConfigurationReader instance;

	private AbcdConfigurationReader() {
		super();

		addProperty(ID_NUMBER_REGEX, DEFAULT_NUMBER_REGEX);
		addProperty(ID_DATE_REGEX, DEFAULT_DATE_REGEX);
		addProperty(ID_POSTAL_REGEX, DEFAULT_POSTAL_REGEX);
		addProperty(ID_ISSUE_MANAGER_URL, DEFAULT_ISSUE_MANAGER_URL);

		addPropertiesSource(new PropertiesSourceFile(DATABASE_CONFIG_FILE));
		addPropertiesSource(new SystemVariablePropertiesSourceFile(ABCD_SYSTEM_VARIABLE_CONFIG, DATABASE_CONFIG_FILE));

		readConfigurations();
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

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public String getNumberMask() {
		return getPropertyLogException(ID_NUMBER_REGEX);
	}

	public String getDateMask() {
		return getPropertyLogException(ID_DATE_REGEX);
	}

	public String getPostalCodeMask() {
		return getPropertyLogException(ID_POSTAL_REGEX);
	}

	public String getIssueManagerUrl() {
		return getPropertyLogException(ID_ISSUE_MANAGER_URL);
	}
}
