package com.biit.abcd.pdfgenerator;

import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * Generator for text area. All text areas are defined with the same height.
 *
 */
class FormTextArea extends FormTextField {
	private static final Integer MAX_HEIGHT = 45;

	public FormTextArea(PdfWriter writer, String fieldName) {
		super(writer, fieldName);
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		TextField tf = createTextField(position);
		tf.setOptions(TextField.MULTILINE);

		try {
			PdfFormField field = tf.getTextField();
			writer.addAnnotation(field);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Integer getHeight() {
		return MAX_HEIGHT;
	}
}
