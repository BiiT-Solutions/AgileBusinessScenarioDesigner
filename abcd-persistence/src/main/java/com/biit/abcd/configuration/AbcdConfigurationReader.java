package com.biit.abcd.configuration;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.utils.annotations.FindBugsSuppressWarnings;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

public class AbcdConfigurationReader extends ConfigurationReader {

	private static final String DATABASE_CONFIG_FILE = "settings.conf";
	private static final String ABCD_SYSTEM_VARIABLE_CONFIG = "ABCD_CONFIG";
	
	private static final String ID_GRAPHVIZ_PATH = "graphvizBinPath";

	private static final String PDF_CREATOR_FEATURE = "feature.pdf.enabled";

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
	private static final String DEFAULT_PDF_CREATOR_FEATURE = "false";
	private static final String DEFAULT_GRAPHVIZ_PATH = "/usr/bin/dot";
	private static final String KNOWLEDGE_MANAGER_SERVICE_PUBLISH_URL = "knowledge-manager.publish.url";
	private static final String KNOWLEDGE_MANAGER_SERVICE_LOGIN_URL = "knowledge-manager.login.url";
	private static final String DEFAULT_KNOWLEDGE_MANAGER_SERVICE_PUBLISH_URL = "http://localhost:8085/knowledge-manager/forms/add-form";
	private static final String DEFAULT_KNOWLEDGE_MANAGER_SERVICE_LOGIN_URL = "http://localhost:8085/knowledge-manager/api/public/login";

	private static AbcdConfigurationReader instance;

	private AbcdConfigurationReader() {
		super();

		addProperty(ID_NUMBER_REGEX, DEFAULT_NUMBER_REGEX);
		addProperty(ID_DATE_REGEX, DEFAULT_DATE_REGEX);
		addProperty(ID_POSTAL_REGEX, DEFAULT_POSTAL_REGEX);
		addProperty(ID_ISSUE_MANAGER_URL, DEFAULT_ISSUE_MANAGER_URL);
		addProperty(PDF_CREATOR_FEATURE, DEFAULT_PDF_CREATOR_FEATURE);
		addProperty(ID_GRAPHVIZ_PATH, DEFAULT_GRAPHVIZ_PATH);
		addProperty(KNOWLEDGE_MANAGER_SERVICE_LOGIN_URL, DEFAULT_KNOWLEDGE_MANAGER_SERVICE_LOGIN_URL);
		addProperty(KNOWLEDGE_MANAGER_SERVICE_PUBLISH_URL, DEFAULT_KNOWLEDGE_MANAGER_SERVICE_PUBLISH_URL);

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

	public boolean isPdfEnabled() {
		try {
			return Boolean.parseBoolean(getPropertyLogException(PDF_CREATOR_FEATURE));
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getGraphvizBinPath() {
		return getPropertyLogException(ID_GRAPHVIZ_PATH);
	}

	public String getKnowledgeManagerServiceLoginUrl() {
		return getPropertyLogException(KNOWLEDGE_MANAGER_SERVICE_LOGIN_URL);
	}

	public String getKnowledgeManagerServicePublishUrl() {
		return getPropertyLogException(KNOWLEDGE_MANAGER_SERVICE_PUBLISH_URL);
	}
}
