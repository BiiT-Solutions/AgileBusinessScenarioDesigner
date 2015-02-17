package com.biit.abcd.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import com.biit.abcd.logger.AbcdLogger;

@SuppressWarnings("rawtypes")
public class JpaSchemaExporter {
	private static final String DATABASE_NAME = "abcd";
	private static final String DEFAULT_DATABASE_HOST_NAME = "localhost";
	private static final String DEFAULT_DATABASE_PORT = "3306";
	private static final String DEFAULT_DATABASE_USER = "testuser";
	private static final String DEFAULT_DATABASE_PASSWORD = "asd123";
	private static final String PACKETS_TO_SCAN[] = { "com.biit.abcd.persistence.entity" };
	private static final String DEFAULT_OUTPUT_DIRECTORY = "../abcd-release/database/";
	private static final String[] TABLES_TO_MODIFY = new String[] { "tree_forms" };
	private static final String DATABASE_FILE = "create_" + HibernateDialect.MYSQL.name().toLowerCase() + ".sql";
	private Configuration cfg;

	public JpaSchemaExporter(String[] packagesName) {
		cfg = new Configuration();
		try {
			for (int i = 0; i < packagesName.length; i++) {
				for (Class clazz : getClasses(packagesName[i])) {
					cfg.addAnnotatedClass(clazz);
				}
			}
		} catch (Exception e) {
			AbcdLogger.errorMessage(JpaSchemaExporter.class.getName(), e);
		}
	}

	/**
	 * Utility method used to fetch Class list based on a package name.
	 * 
	 * @param packageName
	 *            should be the package containing your annotated beans.
	 */
	private List<Class> getClasses(String packageName) throws Exception {
		File directory = null;
		try {
			ClassLoader cld = getClassLoader();
			URL resource = getResource(packageName, cld);
			directory = new File(resource.getFile());
		} catch (NullPointerException ex) {
			throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
		}
		return collectClasses(packageName, directory);
	}

	private ClassLoader getClassLoader() throws ClassNotFoundException {
		ClassLoader cld = Thread.currentThread().getContextClassLoader();
		if (cld == null) {
			throw new ClassNotFoundException("Can't get class loader.");
		}
		return cld;
	}

	private URL getResource(String packageName, ClassLoader cld) throws ClassNotFoundException {
		String path = packageName.replace('.', '/');
		URL resource = cld.getResource(path);
		if (resource == null) {
			throw new ClassNotFoundException("No resource for " + path);
		}
		return resource;
	}

	private List<Class> collectClasses(String packageName, File directory) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<>();
		if (directory.exists()) {
			String[] files = directory.list();
			for (String fileName : files) {
				File subdirectory = new File(directory.getPath() + File.separator + fileName);
				if (fileName.endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6)));
				} else if (subdirectory.isDirectory()) {
					// Subpacket.
					classes.addAll(collectClasses(packageName + '.' + fileName, subdirectory));
				}
			}
		} else {
			throw new ClassNotFoundException(packageName + " is not a valid package");
		}
		return classes;
	}

	/**
	 * Create a script that can generate a database for the selected dialect
	 * 
	 * @param dialect
	 * @param directory
	 */
	public void createDatabaseScript(HibernateDialect dialect, String directory, boolean onlyCreation) {
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());
		cfg.setProperty("hibernate.show_sql", "false");
		SchemaExport export = new SchemaExport(cfg);
		export.setDelimiter(";");
		export.setOutputFile(directory + File.separator + DATABASE_FILE);
		export.setFormat(true);
		export.execute(true, false, false, onlyCreation);
		updateTables(directory);

	}

	/**
	 * Changes some Hibernate wrong definitions.
	 */
	private void updateTables(String directory) {
		for (String tableName : TABLES_TO_MODIFY) {
			replaceFormLabel(tableName, directory);
		}
	}

	private void replaceFormLabel(String tableName, String directory) {
		String oldFileName = directory + File.separator + DATABASE_FILE;
		String tmpFileName = System.getProperty("java.io.tmpdir") + File.separator + DATABASE_FILE;

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

	public void updateDatabaseScript(HibernateDialect dialect, String outputDirectory, String host, String port,
			String username, String password) {
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		cfg.setProperty("hibernate.dialect", dialect.getDialectClass());
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.connection.driver_class", dialect.getDriver());
		cfg.setProperty("hibernate.connection.url", "jdbc:mysql://" + host + ":" + port + "/" + DATABASE_NAME);
		cfg.setProperty("hibernate.connection.username", username);
		cfg.setProperty("hibernate.connection.password", password);

		SchemaUpdate update = new SchemaUpdate(cfg);
		update.setDelimiter(";");
		update.setOutputFile(outputDirectory + "updates/update_" + dialect.name().toLowerCase() + "_" + getDate()
				+ ".sql");
		update.setFormat(true);
		update.execute(true, true);
	}

	/**
	 * For executing.
	 * 
	 * @param args
	 *            args[0] -> directory, args[1] -> user, args[2] -> password, args[3] -> host, args[4] -> port
	 */
	public static void main(String[] args) {
		JpaSchemaExporter gen = new JpaSchemaExporter(PACKETS_TO_SCAN);
		String directory;
		if (args.length == 0) {
			directory = DEFAULT_OUTPUT_DIRECTORY;
		} else {
			directory = args[0] + File.separator;
		}

		String user;
		if (args.length > 0) {
			user = DEFAULT_DATABASE_USER;
		} else {
			user = args[1];
		}
		String password;
		if (args.length > 1) {
			password = DEFAULT_DATABASE_PASSWORD;
		} else {
			password = args[2];
		}

		String host;
		if (args.length > 2) {
			host = DEFAULT_DATABASE_HOST_NAME;
		} else {
			host = args[3];
		}

		String port;
		if (args.length > 3) {
			port = DEFAULT_DATABASE_PORT;
		} else {
			port = args[4];
		}
		gen.createDatabaseScript(HibernateDialect.MYSQL, directory, true);
		gen.updateDatabaseScript(HibernateDialect.MYSQL, directory, host, port, user, password);

		addTextToFile(createHibernateSequenceTable(), directory + File.separator + DATABASE_FILE);
	}

	private static String getDate() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy_HHmmss");
		return formateador.format(ahora);
	}

	private static String createHibernateSequenceTable() {
		String table = "\n\tCREATE TABLE `hibernate_sequence` (\n";
		table += "\t\t`next_val` bigint(20) DEFAULT NULL\n";
		table += "\t);\n\n";
		table += "\tLOCK TABLES `hibernate_sequence` WRITE;\n";
		table += "\tINSERT INTO `hibernate_sequence` VALUES (1);\n";
		table += "\tUNLOCK TABLES;\n";
		return table;
	}

	private static void addTextToFile(String text, String file) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
			out.println(text);
		} catch (IOException e) {
		}
	}

}
