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

import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfCol;
import com.biit.abcd.persistence.entity.Question;
import com.lowagie.text.pdf.PdfPCell;

/**
 * Class to encapsulate in a regular column elements of the form.
 *
 */
public class PdfColGenerator {

	private static final int RADIO_FIELD_NAME_COL = 1;

	public static PdfCol generateMultiFieldNameCol(Question question,int rowSpan) throws BadBlockException {

		PdfCol nameCol = new PdfCol(rowSpan, RADIO_FIELD_NAME_COL);
		
		PdfPCell nameCell = PdfPCellGenerator.generateFormQuestionNameCell(question);
		nameCell.setRowspan(rowSpan);
		nameCol.setCell(nameCell);
		
		return nameCol;
	}

}
