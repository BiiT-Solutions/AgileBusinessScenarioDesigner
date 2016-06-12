package com.biit.abcd.pdfgenerator;

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * Generator for text fields. All the elements have a font size and height
 * defined as default
 *
 */
class FormTextField implements PdfPCellEvent {
	private static final Integer FONT_SIZE = 12;
	private static final Integer MAX_HEIGHT = 20;
	protected PdfWriter writer;
	protected String fieldname;

	public FormTextField(PdfWriter writer, String fieldname) {
		this.writer = writer;
		this.fieldname = fieldname;
	}

	protected static Rectangle getLimitedRectangle(Rectangle position, int maxHeight) {
		return new Rectangle(position.getLeft(), position.getTop() - maxHeight, position.getRight(), position.getTop());
	}

	protected TextField createTextField(Rectangle position) {
		TextField tf = new TextField(writer, getLimitedRectangle(position, getHeight()), fieldname);
		tf.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
		tf.setBorderColor(Color.BLACK);
		tf.setBorderWidth((float) 0.5);
		tf.setFontSize(FONT_SIZE);

		return tf;
	}

	protected Integer getHeight() {
		return MAX_HEIGHT;
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		TextField tf = createTextField(position);

		try {
			PdfFormField field = tf.getTextField();
			writer.addAnnotation(field);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
