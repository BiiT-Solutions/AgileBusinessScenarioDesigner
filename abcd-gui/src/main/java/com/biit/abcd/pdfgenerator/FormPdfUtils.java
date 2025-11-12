package com.biit.abcd.pdfgenerator;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lowagie.text.DocumentException;

/**
 * Class with utility methods
 *
 */
public class FormPdfUtils {

	public static InputStream generatePdf(DocumentGenerator generator) throws IOException, DocumentException {
		if (generator != null) {
			byte[] data = generator.generate();
			// convert array of bytes into file
			InputStream inputStream = new ByteArrayInputStream(data);
			return inputStream;
		}
		return null;
	}

	public static void generatePdfAsFile(String filename, DocumentGenerator generator) throws IOException,
			FileNotFoundException, DocumentException {
		if (generator != null) {
			byte[] data = generator.generate();
			// convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream(filename);
			fileOuputStream.write(data);
			fileOuputStream.close();
		}
	}
}
