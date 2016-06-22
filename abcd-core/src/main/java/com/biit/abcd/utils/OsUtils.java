package com.biit.abcd.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import com.biit.abcd.utils.exceptions.ExecutableCanNotBeExecuted;
import com.biit.abcd.utils.exceptions.PathToExecutableNotFound;

/**
 * Helper class for several Operative system file related issues.
 *
 */
public class OsUtils {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static String getProgramsFolder() {
		if (isWindows()) {
			String path = System.getenv("ProgramFiles(x86)") != null ? System.getenv("ProgramFiles(x86)") : System
					.getenv("ProgramFiles");
			return path;
		} else if (isMac()) {
			return File.separator + "usr" + File.separator + "bin";
		} else if (isUnix()) {
			return File.separator + "usr" + File.separator + "bin";
		} else if (isSolaris()) {
			return File.separator + "usr" + File.separator + "bin";
		} else {
			return "";
		}
	}

	public static String readPropertiesValue(String fileName, String property) throws IOException {
		Properties prop = new Properties();
		String propFileName = fileName;
		if (!propFileName.endsWith(".properties")) {
			propFileName += ".properties";
		}

		InputStream inputStream = OsUtils.class.getClassLoader().getResourceAsStream(propFileName);
		if (inputStream == null) {
			throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
		}
		prop.load(inputStream);

		return prop.getProperty(property);
	}

	public static String findExecutablePropertiesFile(String fileName, String property) throws FileNotFoundException,
			IOException, ExecutableCanNotBeExecuted, PathToExecutableNotFound {

		String executablePath = readPropertiesValue(fileName, property);
		checkExecutable(executablePath);
		return executablePath;
	}

	public static String findExecutableEnvironmentVariable(String environmentVariable) throws PathToExecutableNotFound,
			ExecutableCanNotBeExecuted {
		String path = System.getenv(environmentVariable);

		if (path != null && !path.isEmpty()) {
			checkExecutable(path);
			return path;
		}
		throw new PathToExecutableNotFound();
	}

	public static String findExecutablePath(String executableName) throws ExecutableCanNotBeExecuted,
			PathToExecutableNotFound {
		String basicPath = getProgramsFolder();
		String filePath = new String();

		if (isWindows()) {
			filePath = basicPath + File.separator + executableName + ".exe";
		}
		if (isUnix() || isMac() || isSolaris()) {
			filePath = basicPath + File.separator + executableName;
		}

		checkExecutable(filePath);
		return filePath;
	}

	public static void checkExecutable(String executablePath) throws ExecutableCanNotBeExecuted,
			PathToExecutableNotFound {
		File executable = new File(executablePath);
		if (executable.exists()) {
			if (!executable.canExecute()) {
				throw new ExecutableCanNotBeExecuted();
			}
		} else {
			throw new PathToExecutableNotFound();
		}
	}

	public static void execSynchronic(String... stringArgs) throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(stringArgs);
		p.waitFor();
	}

	/**
	 * Writes a temporal file with a string content
	 * 
	 * @param prefix
	 * @param sufix
	 * @param string
	 * @return
	 * @throws IOException
	 */
	public static File writeInTempFile(String prefix, String sufix, String string) throws IOException {
		File temp;
		temp = File.createTempFile(prefix, sufix);

		FileOutputStream fileStream = new FileOutputStream(temp);
		OutputStreamWriter writer = new OutputStreamWriter(fileStream, "UTF-8");

		writer.write(string);
		writer.close();
		return temp;
	}
}
