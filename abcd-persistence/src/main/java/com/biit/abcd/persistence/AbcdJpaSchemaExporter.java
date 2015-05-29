package com.biit.abcd.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.persistence.HibernateDialect;
import com.biit.persistence.JpaSchemaExporter;

/**
 * Extends JpaSchemaExporter for updating the form label from 1000 chars to 190.
 */
public class AbcdJpaSchemaExporter extends com.biit.persistence.JpaSchemaExporter {
	private static final String[] TABLES_TO_MODIFY = new String[] { "tree_forms", "test_scenario" };

	public AbcdJpaSchemaExporter(String[] packagesName, String[] classesToIgnore) {
		super(packagesName, classesToIgnore);
	}

	/**
	 * Create a script that can generate a database for the selected dialect
	 * 
	 * @param dialect
	 * @param directory
	 */
	@Override
	public void createDatabaseScript(HibernateDialect dialect, String directory, String outputFile, boolean onlyCreation) {
		super.createDatabaseScript(dialect, directory, outputFile, onlyCreation);
		updateTables(directory, outputFile);
	}

	/**
	 * Changes some Hibernate wrong definitions.
	 */
	private void updateTables(String directory, String outputFile) {
		for (String tableName : TABLES_TO_MODIFY) {
			replaceFormLabel(tableName, directory, outputFile);
		}
	}

	private void replaceFormLabel(String tableName, String directory, String outputFile) {
		String oldFileName = directory + File.separator + outputFile;
		String tmpFileName = System.getProperty("java.io.tmpdir") + File.separator + outputFile;

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(tmpFileName));
			String line;
			boolean correctTable = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("create table ")) {
					// Starting of a table.
					correctTable = false;
					if (line.contains("create table " + tableName + " (")) {
						// Starting of the correct table.
						correctTable = true;
					}
				}
				if (correctTable && line.contains("label varchar(1000)")) {
					line = line.replace("label varchar(1000)", "label varchar(190)");
				}
				if (correctTable && line.contains("formLabel varchar(1000)")) {
					line = line.replace("formLabel varchar(1000)", "formLabel varchar(190)");
				}
				bw.write(line + "\n");
			}
		} catch (Exception e) {
			return;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}
		// Once everything is complete, delete old file..
		File oldFile = new File(oldFileName);
		oldFile.delete();

		// And rename tmp file's name to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile);
	}

	public static void main(String[] args) {
		setArguments(args);

		// Launch the JpaSchemaExporter
		JpaSchemaExporter gen = new AbcdJpaSchemaExporter(getPacketsToScan(), getClassesToIgnoreWhenCreatingDatabase());
		gen.createDatabaseScript(HibernateDialect.MYSQL, getDirectory(), getOutputFile(), true);
		gen = new JpaSchemaExporter(getPacketsToScan(), getClassesToIgnoreWhenUpdatingDatabase());
		gen.updateDatabaseScript(HibernateDialect.MYSQL, getDirectory(), getHost(), getPort(), getUser(),
				getPassword(), getDatabaseName());

		// Add hibernate sequence table.
		addTextToFile(createHibernateSequenceTable(), getDirectory() + File.separator + getOutputFile());
		// Add extra information from a external script.
		addTextToFile(readFile(getScriptsToAdd(), Charset.forName("UTF-8")), getDirectory() + File.separator
				+ getOutputFile());
	}
}
