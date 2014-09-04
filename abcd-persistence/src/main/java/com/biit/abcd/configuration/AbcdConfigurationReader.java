package com.biit.abcd.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;

public class AbcdConfigurationReader {
	private final String DATABASE_CONFIG_FILE = "settings.conf";

	private final String POSTALCODE_TAG = "postalCode";

	private final String DEFAULT_POSTAL_CODE = "[0-9]{4}[a-zA-Z]{2}";

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
			postalCodeMask = prop.getProperty(POSTALCODE_TAG);
		} catch (IOException e) {
			// Do nothing.
		}

		if (postalCodeMask == null) {
			postalCodeMask = DEFAULT_POSTAL_CODE;
		}

	}

	public String getPostalCodeMask() {
		return postalCodeMask;
	}

}
