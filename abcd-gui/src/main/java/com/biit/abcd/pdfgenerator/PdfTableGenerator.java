package com.biit.abcd.pdfgenerator;

import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfTableBlock;
import com.biit.abcd.persistence.entity.Form;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Utility class to generate pdfptables depending on form elements.
 *
 */
public class PdfTableGenerator {

	private final static float[] annexFormColumnRatios = { 0.34f, 0.34f, 0.16f, 0.16f };
	private static final float[] FORM_VARIABLE_COLUMN_RATIOS = { 0.25f, 0.25f, 0.25f, 0.25f };
	private static final float[] EXPRESSIONS_TABLE_RATIOS = { 1.0f };
	private static final float[] RULE_TABLE_RATIOS = { 1.0f };

	public static PdfPTable generateTable(float relativeWidths[], List<PdfTableBlock> tableBlocks) throws BadBlockException {
		PdfPTable table = new PdfPTable(relativeWidths);
		table.setSplitRows(false);
		table.getDefaultCell().enableBorderSide(Rectangle.TOP);
		table.getDefaultCell().enableBorderSide(Rectangle.BOTTOM);
		table.getDefaultCell().enableBorderSide(Rectangle.LEFT);
		table.getDefaultCell().enableBorderSide(Rectangle.RIGHT);

		// Check uniformity on table.
		// if (!checkUniformity(relativeWidths.length, tableBlocks)) {
		// throw new BadBlockException();
		// }

		for (PdfTableBlock block : tableBlocks) {
			List<PdfPCell> cells = block.getCells();
			for (PdfPCell cell : cells) {
				table.addCell(cell);
			}
		}

		return table;
	}

	public static void generate(Document document, float relativeWidths[], List<PdfTableBlock> tableBlocks) throws DocumentException {
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

	public static PdfPTable generateStructureFormTable(Form form) {
		PdfPTable table = null;
		try {
			table = generateTable(annexFormColumnRatios, PdfBlockGenerator.generateAnnexFormTableBlocks(form));
			table.getDefaultCell().setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}

	public static Element generateFormVariablesTable(Form form) {
		PdfPTable table = null;
		try {
			table = generateTable(FORM_VARIABLE_COLUMN_RATIOS, PdfBlockGenerator.generateFormVariableTableBlocks(form));
			PdfPCell kiwi = new PdfPCell(ParagraphGenerator.generateTextParagraph("KIWIIIIIIIII"));
			kiwi.setColspan(4);
			table.addCell(kiwi);
			System.out.println("jodeeeeer");
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}

	public static Element generateExpressionsTable(Form form) {
		PdfPTable table = null;
		try {
			table = generateTable(EXPRESSIONS_TABLE_RATIOS, PdfBlockGenerator.generateExpressionTableBlocks(form));
			table.getDefaultCell().setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}

	public static Element generateRulesTable(Form form) {
		PdfPTable table = null;
		try {
			table = generateTable(RULE_TABLE_RATIOS, PdfBlockGenerator.generateRuleTableBlocks(form));
			table.getDefaultCell().setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}
}
