package com.biit.abcd.pdfgenerator;

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
public class FormGeneratorPdf {

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
