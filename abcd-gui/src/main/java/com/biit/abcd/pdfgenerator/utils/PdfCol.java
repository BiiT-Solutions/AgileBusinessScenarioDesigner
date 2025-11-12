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

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.lowagie.text.pdf.PdfPCell;

/**
 * Definition of elements that can occupy several elements in a column.
 *
 */
public class PdfCol implements IPdfTableBlock {

	private int numberRows;
	private int numberCols;

	private List<PdfPCell> cells;

	public PdfCol(int numberRows, int numberCols) {
		this.numberRows = numberRows;
		this.numberCols = numberCols;
		cells = new ArrayList<PdfPCell>();
	}

	public void setCell(PdfPCell cell) throws BadBlockException {
		if (cell.getRowspan() != numberRows || cell.getColspan() != numberCols) {
			throw new BadBlockException();
		} else {
			cells.clear();
			cells.add(cell);
		}
	}

	@Override
	public boolean isWellFormatted() {
		return (!cells.isEmpty()) && cells.get(0).getRowspan() == numberRows && cells.get(0).getColspan() == numberCols;
	}

	@Override
	public int getNumberRows() {
		return numberRows;
	}

	@Override
	public int getNumberColumns() {
		return numberCols;
	}

	@Override
	public List<PdfPCell> getCells() {
		return cells;
	}

	public static PdfCol generateWhiteCol(int numberRows, int numberCols) {
		PdfCol col = new PdfCol(numberRows, numberCols);
		PdfPCell cell = new PdfPCell();
		cell.setRowspan(numberRows);
		cell.setColspan(numberCols);
		try {
			col.setCell(cell);
		} catch (BadBlockException e) {
			// Impossible
			AbcdLogger.errorMessage(PdfCol.class.getName(), e);
		}
		return col;
	}
}
