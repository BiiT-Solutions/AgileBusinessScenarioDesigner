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

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

/**
 * Generator for radio field elements of the forms. Its configured to work in
 * conjuction with the other elements of the radio field.
 *
 */
public class FormRadioField implements PdfPCellEvent {
	private static final float SIZE = 16.0f;
	private PdfWriter writer;
	private String fieldName;
	private String optionName;
	private PdfFormField radioGroup;
	private float leftIndent;

	public FormRadioField(PdfWriter writer, String fieldName, String optionName, PdfFormField radioGroup, float leftIndent) {
		this.writer = writer;
		this.fieldName = fieldName;
		this.optionName = optionName;
		this.radioGroup = radioGroup;
		this.leftIndent = leftIndent;
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		RadioCheckField radio = new RadioCheckField(writer, null, fieldName, optionName);

		float minHeight = Math.min(SIZE, position.getTop() - position.getBottom());
		float yCoordinate = (position.getTop() + position.getBottom()) / 2.0f;

		radio.setBox(new Rectangle(position.getLeft() + leftIndent, yCoordinate - minHeight / 2.0f, position.getLeft() + minHeight
				+ leftIndent, yCoordinate + minHeight / 2.0f));

		radio.setCheckType(RadioCheckField.TYPE_CIRCLE);
		radio.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
		radio.setBorderColor(Color.black);
		radio.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
		radio.setChecked(false);

		try {
			radioGroup.addKid(radio.getRadioField());
			writer.addAnnotation(radioGroup);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
