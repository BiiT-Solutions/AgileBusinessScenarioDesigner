package com.biit.abcd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.utils.exceptions.ExecutableCanNotBeExecuted;
import com.biit.abcd.utils.exceptions.PathToExecutableNotFound;
import com.biit.abcd.utils.exporters.dotgraph.ExporterDotForm;

/**
 * Helper class to generate the graphviz
 *
 */
public class GraphvizApp {

	/* Official variable name but it's not usually set */
	public static String GRAPHVIZ_SYSVAR = "GVBINDIR";
	public static String applicationPath = null;

	public enum ImgType {
		GIF("gif"), DOT("dot"), FIG("fig"), PDF("pdf"), PS("ps"), SVG("svg"), PNG("png"), ;
		private String type;

		private ImgType(String imgType) {
			this.type = imgType;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * Finds path to application. In order we give precedente to the path on the
	 * config file. If not found, (If there was no file we don't say anything
	 * 
	 * @throws PathToExecutableNotFound
	 * @throws ExecutableCanNotBeExecuted
	 */
	private static synchronized void findApplication() throws ExecutableCanNotBeExecuted, PathToExecutableNotFound {
		if (applicationPath == null) {
			applicationPath = AbcdConfigurationReader.getInstance().getGraphvizBinPath();
			if (applicationPath != null) {
				return;
			}
			applicationPath = OsUtils.findExecutableEnvironmentVariable(GRAPHVIZ_SYSVAR);
		}
	}

	/**
	 * This function gets dot code in string form and generates a image of
	 * {@code imgType} using temporal files. When the process is finished all
	 * temporal files are deleted and the result image is returned as a byte
	 * array.
	 * 
	 * @param dotCode
	 *            dot code in string form
	 * @param imgType
	 *            image file format
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static byte[] generateImageFromDotCode(String dotCode, ImgType imgType) throws IOException, InterruptedException {
		byte[] img_stream = null;

		try {
			findApplication();
			// Generates a temp file with the dot Code content
			File dotTemp = OsUtils.writeInTempFile("dotCode_", ".dot.tmp", dotCode);
			// Generates a temp file for the image
			File imgTemp = File.createTempFile("dotImage_", "." + imgType.getType());
			
			// Execution of Graphviz
			String[] args = { applicationPath, "-T" + imgType.getType(), dotTemp.getAbsolutePath(), "-o", imgTemp.getAbsolutePath() };
			OsUtils.execSynchronic(args);

			// Pass data to byte[]

			try (FileInputStream in = new FileInputStream(imgTemp.getAbsolutePath())) {
				img_stream = new byte[in.available()];
				in.read(img_stream);
			}

			// Delete both files.
			if (dotTemp.delete() == false) {
				AbcdLogger.warning(GraphvizApp.class.getName(), dotTemp.getAbsolutePath() + " could not be deleted.");
			}
			if (imgTemp.delete() == false) {
				AbcdLogger.warning(GraphvizApp.class.getName(), imgTemp.getAbsolutePath() + " could not be deleted.");
			}
		} catch (ExecutableCanNotBeExecuted e) {
			AbcdLogger.severe(GraphvizApp.class.getName(), "Executable can't be executed.");
		} catch (PathToExecutableNotFound e) {
			AbcdLogger.severe(GraphvizApp.class.getName(), "Specified path doesn't exist");
		}
		return img_stream;
	}

	/**
	 * Generates the graph image of a form with the type {@code imgType} if
	 * {@code filter} is null no filter is used and all the form is rendered.
	 * 
	 * @param form
	 *            source form
	 * @param filter
	 *            data filter
	 * @param imgType
	 *            image file format
	 * @return Byte data of image
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static byte[] generateImage(Form form, Diagram diagram, ImgType imgType) throws IOException, InterruptedException {
		// Generate DotCode
		String dotCode = null;
		ExporterDotForm exporter = new ExporterDotForm();
		dotCode = exporter.export(diagram);

		// Call Graphviz to make a render from dot code.
		return generateImageFromDotCode(dotCode, imgType);
	}
}
