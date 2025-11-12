package com.biit.abcd.utils;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
