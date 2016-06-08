package com.biit.abcd.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.lowagie.text.pdf.PdfPCell;

/**
 * Container to handle the composition of row and col elements to ensure they
 * are always a consistent block.
 *
 */
public class PdfTableBlock implements IPdfTableBlock {

	private int numberCols;
	private int numberRows;

	private List<IPdfTableBlock> tableBlocks;

	public PdfTableBlock(int numberRows, int numberCols) {
		this.numberCols = numberCols;
		this.numberRows = numberRows;
		tableBlocks = new ArrayList<IPdfTableBlock>();
	}

	public void insertRow(PdfRow row) throws BadBlockException {
		if (!row.isWellFormatted() || (row.getNumberCols() != getRemainingCols()) || (row.getNumberRows() > getRemainingRows())) {
			throw new BadBlockException();
		}
		tableBlocks.add(row);
	}

	public void insertCol(PdfCol col) throws BadBlockException {
		if (!col.isWellFormatted() || (col.getNumberRows() != getRemainingRows()) || (col.getNumberCols() > getRemainingCols())) {
			throw new BadBlockException();
		}
		tableBlocks.add(col);
	}

	public int getNumberCols() {
		return numberCols;
	}

	public int getNumberRows() {
		return numberRows;
	}

	private int getRemainingCols() {
		int remainingCols = numberCols;
		for (IPdfTableBlock block : tableBlocks) {
			if (block instanceof PdfCol) {
				remainingCols -= block.getNumberCols();
			}
		}
		return remainingCols;
	}

	private int getRemainingRows() {
		int remainingRows = numberRows;
		for (IPdfTableBlock block : tableBlocks) {
			if (block instanceof PdfRow) {
				remainingRows -= block.getNumberRows();
			}
		}
		return remainingRows;
	}

	@Override
	public boolean isWellFormatted() {
		return (getRemainingCols() == 0) || (getRemainingRows() == 0);
	}

	@Override
	public List<PdfPCell> getCells() {
		List<PdfPCell> cells = new ArrayList<PdfPCell>();
		for (IPdfTableBlock block : tableBlocks) {
			cells.addAll(block.getCells());
		}
		return cells;
	}
}
