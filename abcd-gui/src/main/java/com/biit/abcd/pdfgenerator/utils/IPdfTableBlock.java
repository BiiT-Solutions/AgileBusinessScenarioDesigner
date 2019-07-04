package com.biit.abcd.pdfgenerator.utils;

import java.util.List;

import com.lowagie.text.pdf.PdfPCell;

/**
 * Interface to define the table partition for the different sections.
 *
 */
public interface IPdfTableBlock {

	/**
	 * Returns the number of column spaces used by this table
	 * 
	 * @return the number of columns.
	 */
	int getNumberColumns();

	/**
	 * Returns the number of row spaces used by this table
	 * 
	 * @return the number of rows.
	 */
	int getNumberRows();

	/**
	 * Returns the well-formed state of the table. If the sum of spaces of the
	 * elements doesn't generate a correct rectangle it returns false.
	 * 
	 * @return true if the format is correct.
	 */
	boolean isWellFormatted();

	/**
	 * Returns all the sub elements of the table.
	 * 
	 * @return a list of cells.
	 */
	List<PdfPCell> getCells();
}
