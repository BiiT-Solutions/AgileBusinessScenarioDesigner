package com.biit.abcd.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;

public class AbcdConfigurationReader {
	private final String DATABASE_CONFIG_FILE = "settings.conf";

	// Regex Tags
	private final String NUMBER_REGEX_TAG = "numberRegEx";
	private final String DATE_REGEX_TAG = "dateRegEx";
	private final String POSTALCODE_REGEX_TAG = "postalCodeRegEx";
	// Prompts Tags
	private final String TEXT_PROMT_TAG = "textPrompt";
	private final String NUMBER_PROMT_TAG = "numberPrompt";
	private final String DATE_PROMT_TAG = "datePromt";
	private final String POSTALCODE_PROMT_TAG = "postalCodePrompt";
	// Defaults
	private final String DEFAULT_TEXT_PROMT = "Text";
	private final String DEFAULT_NUMBER_PROMT = "1,234";
	private final String DEFAULT_DATE_PROMT = "dd/MM/yyyy";
	private final String DEFAULT_POSTAL_CODE_PROMT = "0000AA";
	
	private final String DEFAULT_NUMBER_CODE_REGEX = "[-]?[0-9]*\\.?,?[0-9]+";
	private final String DEFAULT_DATE_CODE_REGEX = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
	private final String DEFAULT_POSTAL_CODE_REGEX = "[0-9]{4}[a-zA-Z]{2}";
	
	private String textPromt;
	private String numberPromt;
	private String datePromt;
	private String postalCodePromt;
	private String numberMask;
	private String dateMask;
	private String postalCodeMask;
	

	private static AbcdConfigurationReader instance;

	private AbcdConfigurationReader() {
		readConfig();
	}

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
			// Set promts
			textPromt = prop.getProperty(TEXT_PROMT_TAG);
			numberPromt = prop.getProperty(NUMBER_PROMT_TAG);
			datePromt = prop.getProperty(DATE_PROMT_TAG);
			postalCodePromt = prop.getProperty(POSTALCODE_PROMT_TAG);
			// Set masks
			numberMask = prop.getProperty(NUMBER_REGEX_TAG);
			dateMask = prop.getProperty(DATE_REGEX_TAG);
			postalCodeMask = prop.getProperty(POSTALCODE_REGEX_TAG);
		} catch (IOException e) {
			// Do nothing.
		}
		checkForNullValues();
	}
	
	private void checkForNullValues(){
		if (textPromt == null) {
			textPromt = DEFAULT_TEXT_PROMT;
		}
		if (numberPromt == null) {
			numberPromt = DEFAULT_NUMBER_PROMT;
		}
		if (datePromt == null) {
			datePromt = DEFAULT_DATE_PROMT;
		}
		if (postalCodePromt == null) {
			postalCodePromt = DEFAULT_POSTAL_CODE_PROMT;
		}
		if (numberMask == null) {
			numberMask = DEFAULT_NUMBER_CODE_REGEX;
		}
		if (dateMask == null) {
			dateMask = DEFAULT_DATE_CODE_REGEX;
		}
		if (postalCodeMask == null) {
			postalCodeMask = DEFAULT_POSTAL_CODE_REGEX;
		}
	}

	public String getTextPromt() {
		return textPromt;
	}

	public String getNumberPromt() {
		return numberPromt;
	}

	public String getDatePromt() {
		return datePromt;
	}
	
	public String getPostalCodePromt() {
		return postalCodePromt;
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
}
