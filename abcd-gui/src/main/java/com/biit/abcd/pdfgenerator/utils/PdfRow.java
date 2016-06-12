package com.biit.abcd.pdfgenerator.utils;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.lowagie.text.pdf.PdfPCell;

/**
 * Definition of elements that can occupy several elements in a row
 *
 */
public class PdfRow implements IPdfTableBlock{

	private int numberRows;
	private int numberCols;

	private List<PdfPCell> cells;

	public PdfRow(int numberRows,int numberCols) {
		this.numberRows = numberRows;
		this.numberCols = numberCols;
		cells = new ArrayList<PdfPCell>();
	}

	public void addCell(PdfPCell cell) throws BadBlockException {
		if(cell.getRowspan()!=numberRows || ((cell.getColspan() + getCurrentCols()) <= numberCols)){
			cells.add(cell);
		}else{
			throw new BadBlockException();
		}
	}
	
	public int getCurrentCols(){
		int currentCols =0;
		for(PdfPCell cell: cells){
			currentCols+=cell.getColspan();
		}
		return currentCols;
	}
	
	public int getNumberCols() {
		return numberCols;
	}

	public int getNumberRows() {
		return numberRows;
	}

	@Override
	public boolean isWellFormatted() {
		return (getCurrentCols()==numberCols) && (!cells.isEmpty());
	}

	@Override
	public List<PdfPCell> getCells() {
		return cells;
	}
}
