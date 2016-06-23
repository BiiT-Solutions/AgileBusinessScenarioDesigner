package com.biit.abcd.pdfgenerator;

import java.io.IOException;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfTableBlock;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.utils.GraphvizApp;
import com.biit.abcd.utils.GraphvizApp.ImgType;
import com.biit.abcd.utils.ImageManipulator;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Utility class to generate pdfptables depending on form elements.
 *
 */
public class PdfTableGenerator {

	private final static float[] annexFormColumnRatios = { 0.28f, 0.28f, 0.22f, 0.22f };
	private static final float[] FORM_VARIABLE_COLUMN_RATIOS = { 0.35f, 0.15f, 0.15f, 0.15f };
	private static final float[] EXPRESSIONS_TABLE_RATIOS = { 1.0f };
	private static final float[] RULE_TABLE_RATIOS = { 1.0f };
	private static final float[] RULE_TABLE_TABLE_RATIOS = { 1.0f, 1.0f };
	private static final float[] RULE_TABLE_DIAGRAM = { 1.0f };

	public static PdfPTable generateTable(float relativeWidths[], List<PdfTableBlock> tableBlocks) throws BadBlockException {
		PdfPTable table = new PdfPTable(relativeWidths);
		table.setSplitRows(false);
		table.getDefaultCell().enableBorderSide(Rectangle.TOP);
		table.getDefaultCell().enableBorderSide(Rectangle.BOTTOM);
		table.getDefaultCell().enableBorderSide(Rectangle.LEFT);
		table.getDefaultCell().enableBorderSide(Rectangle.RIGHT);

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
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}

	public static Element generateDiagrams(Document document, Form form, Diagram diagram) {
		PdfPTable table = new PdfPTable(RULE_TABLE_DIAGRAM);
		try {
			
			byte[] imageSVG = GraphvizApp.generateImage(form, diagram, ImgType.SVG);
			// Convert to PNG.
			byte[] imagePNG = ImageManipulator.svgToPng(imageSVG);
			
			Image diagramImage = Image.getInstance(imagePNG);
//			int dstHeight, dstWidth;
//			if (((double) diagramImage.getHeight()) / (PageSize.A4.getHeight()) < ((double) diagramImage.getWidth()) / PageSize.A4.getWidth()) {
//				dstWidth = (int) PageSize.A4.getWidth();
//				dstHeight = (int) (diagramImage.getHeight() * PageSize.A4.getWidth() / (double) diagramImage.getWidth());
//			} else {
//				dstWidth = (int) (diagramImage.getWidth() * (PageSize.A4.getHeight()) / (double) diagramImage.getHeight());
//				dstHeight = (int) (PageSize.A4.getHeight());
//			}
			
//			System.out.println(dstWidth + " x " + dstHeight);
			
			float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
			float documentHeight = (document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin())/2;
			diagramImage.scaleToFit(documentWidth, documentHeight);


//			diagramImage.scaleAbsolute(dstWidth, dstHeight);

			PdfPCell cell = new PdfPCell(diagramImage);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
		} catch (BadElementException | IOException | InterruptedException | TranscoderException e) {
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

	public static Element generateRuleTableTable(TableRule table) {
		PdfPTable tableRule = null;
		try {
			tableRule = generateTable(RULE_TABLE_TABLE_RATIOS, PdfBlockGenerator.generateTableBlocks(table));
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return tableRule;
	}
}
