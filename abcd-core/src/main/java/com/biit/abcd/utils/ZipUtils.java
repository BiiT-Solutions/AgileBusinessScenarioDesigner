package com.biit.abcd.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	public static final String FILE_CODIFICATION = "UTF-8";

	public static byte[] createZipFile(List<String> files, List<String> names) throws IOException {
		if (files != null && names != null && files.size() > 0 && files.size() == names.size()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);
			for (int i = 0; i < files.size(); i++) {
				String fileToZip = files.get(i);
				ZipEntry entry = new ZipEntry(names.get(i));
				entry.setSize(fileToZip.length());
				zos.putNextEntry(entry);
				zos.write(fileToZip.getBytes(FILE_CODIFICATION));
			}
			zos.closeEntry();
			zos.close();
			return baos.toByteArray();
		}
		return null;
	}
}
