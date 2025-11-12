package com.biit.abcd.pdfgenerator.utils;

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
