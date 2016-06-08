package com.biit.abcd.pdfgenerator;

import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.persistence.entity.Form;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Utility class to generate pdfptables depending on form elements.
 *
 */
public class PdfTableGenerator {

	private final static float[] annexFormColumnRatios = { 0.34f, 0.18f, 0.16f, 0.16f, 0.16f };

	public static PdfPTable generateTable(float relativeWidths[], List<PdfTableBlock> tableBlocks)
			throws BadBlockException {
		PdfPTable table = new PdfPTable(relativeWidths);
		table.setSplitRows(false);

		// Check uniformity on table.
		if (!checkUniformity(relativeWidths.length, tableBlocks)) {
			throw new BadBlockException();
		}

		for (PdfTableBlock block : tableBlocks) {
			List<PdfPCell> cells = block.getCells();
			for (PdfPCell cell : cells) {
				table.addCell(cell);
			}
		}

		return table;
	}

	public static void generate(Document document, float relativeWidths[], List<PdfTableBlock> tableBlocks)
			throws DocumentException {
		PdfPTable elementTable = new PdfPTable(relativeWidths);
		elementTable.setSplitRows(false);

	}

	private static boolean checkUniformity(int number, List<PdfTableBlock> tableBlocks) {
		for (PdfTableBlock block : tableBlocks) {
			if (!block.isWellFormatted() || (number != block.getNumberCols())) {
				return false;
			}
		}
		return true;
	}

	public static PdfPTable generateAnnexFormTable(Form form) {
		PdfPTable table = null;
		try {
			table = generateTable(annexFormColumnRatios, PdfBlockGenerator.generateAnnexFormTableBlocks(form));
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}
}
