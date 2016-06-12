package com.biit.abcd.pdfgenerator;

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
