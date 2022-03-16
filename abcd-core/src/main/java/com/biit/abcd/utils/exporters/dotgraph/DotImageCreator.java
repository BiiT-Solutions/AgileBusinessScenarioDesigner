package com.biit.abcd.utils.exporters.dotgraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.utils.file.FileReader;

public class DotImageCreator {
	private static final String ICON_RESOURCE_FOLDER = "diagramIcons";
	private static Path ICON_TEMP_FOLDER = null;
	
	public static Path getIconFolder() {
		if(ICON_TEMP_FOLDER==null) {
			try {
				ICON_TEMP_FOLDER = Files.createTempDirectory(ICON_RESOURCE_FOLDER);
				recursiveDeleteOnShutdownHook(ICON_TEMP_FOLDER);
				// Prepare images
				for (DiagramObjectType type : DiagramObjectType.values()) {
					createImageInFolder(type);
				}
			} catch (IOException e) {
				AbcdLogger.errorMessage(DotImageCreator.class.getName(), e);
			}
		}
		return ICON_TEMP_FOLDER;
	}

	public static void recursiveDeleteOnShutdownHook(final Path path) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							Files.delete(file);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
							if (e == null) {
								Files.delete(dir);
								return FileVisitResult.CONTINUE;
							}
							// directory iteration failed
							throw e;
						}
					});
				} catch (IOException e) {
					throw new RuntimeException("Failed to delete " + path, e);
				}
			}
		}));
	}

	private static void storeImageInFile(String imageCode, String imageFile) {
		try (PrintStream out = new PrintStream(new FileOutputStream(getIconFolder() + File.separator + imageFile), true,  StandardCharsets.UTF_8.name())) {
			out.print(imageCode);
			out.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			AbcdLogger.errorMessage(DotImageCreator.class.getName(), e);
		}
	}

	public static void createImageInFolder(DiagramObjectType type) throws IOException {
		File imageFile = new File(getIconFolder().toString() + File.separator + type.getGraphvizIcon());
		if (!imageFile.exists()) {
			String svgContent = FileReader.getResource(ICON_RESOURCE_FOLDER + File.separator + type.getGraphvizIcon(), StandardCharsets.UTF_8);
			storeImageInFile(svgContent, type.getGraphvizIcon());
		}
	}
}
