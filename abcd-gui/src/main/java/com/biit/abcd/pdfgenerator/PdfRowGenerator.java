package com.biit.abcd.pdfgenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.exceptions.BadBlockException;
import com.biit.abcd.pdfgenerator.utils.PdfRow;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.TreeObject;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

/**
 * Utility class to generate pdf row elements.
 *
 */
public class PdfRowGenerator {

	protected static final int RADIO_FIELD_ROW = 1;
	private static final int RADIO_FIELD_COL = 1;
	private static int TEXT_FIELD_ROW = 1;
	private static int TEXT_FIELD_COL = 2;
	private final static float PADDING = 20;
	private static final int MIN_TITLE_ROW = 1;

	public static PdfRow generateAnnexAnswer(BaseAnswer answer) {
		PdfPCell whiteCell = PdfPCellGenerator.generateEmptyCell(1);
		PdfPCell labelCell = PdfPCellGenerator.generateLabelCell(answer);
		labelCell.setColspan(1);
		PdfPCell nameCell = PdfPCellGenerator.generateNameCell(answer);
		nameCell.setColspan(2);

		// Annex answer have one column less than the questions.
		PdfRow answerRow = new PdfRow(PdfBlockGenerator.MIN_ANSWER_ROWS, PdfBlockGenerator.STRUCTURE_COLS);
		try {
			answerRow.addCell(whiteCell);
			answerRow.addCell(labelCell);
			answerRow.addCell(nameCell);
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return answerRow;
	}

	public static PdfRow generateQuestion(Question question) {
		PdfRow row = new PdfRow(PdfBlockGenerator.QUESTION_ROW, PdfBlockGenerator.STRUCTURE_COLS);

		PdfPCell cellLabel = PdfPCellGenerator.generateLabelCell(question);
		PdfPCell cellName = PdfPCellGenerator.generateNameCell(question);

		try {
			row.addCell(cellLabel);
			row.addCell(cellName);
			PdfPCell cellAnswerFormat = PdfPCellGenerator.generateAnswerFormatParagraph(question);
			row.addCell(cellAnswerFormat);
			PdfPCell cellAnswerSubformat = PdfPCellGenerator.generateAnswerSubformatParagraph(question);
			row.addCell(cellAnswerSubformat);

			System.out.println(cellLabel.getColspan());
			System.out.println(cellName.getColspan());
			System.out.println(cellAnswerFormat.getColspan());
			System.out.println(cellAnswerSubformat.getColspan());
		} catch (BadBlockException e) {
			AbcdLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}

		return row;
	}

	public static PdfRow generateTextFieldRow(PdfWriter writer, Question question) throws BadBlockException {
		PdfRow row = new PdfRow(TEXT_FIELD_ROW, TEXT_FIELD_COL);
		row.addCell(PdfPCellGenerator.generateFormQuestionNameCell(question));
		row.addCell(PdfPCellGenerator.generateInputFieldCell(writer, question));
		return row;
	}

	/**
	 * Generates the list of rows needed to generate a Radio button field
	 * 
	 * @param writer
	 * @param question
	 * @return List of PdfRow of sizes RADIO_FIELD_ROW = 1; RADIO_FIELD_COL = 1
	 * @throws BadBlockException
	 */
	public static List<PdfRow> generateRadioFieldRows(PdfWriter writer, Question question) throws BadBlockException {
		List<PdfRow> rows = new ArrayList<PdfRow>();

		RadioCheckField bt = new RadioCheckField(writer, null, question.getComparationId(), "");
		bt.setCheckType(RadioCheckField.TYPE_CIRCLE);
		bt.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
		bt.setBorderColor(Color.black);
		bt.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
		PdfFormField radioGroup = bt.getRadioGroup(false, true);

		for (TreeObject answer : question.getChildren()) {
			List<PdfRow> newRows = generateRadioFieldRows(writer, radioGroup, question, (BaseAnswer) answer);
			rows.addAll(newRows);
		}

		return rows;
	}

	public static List<PdfRow> generateRadioFieldRows(PdfWriter writer, PdfFormField radioGroup, Question question,
			BaseAnswer baseAnswer) throws BadBlockException {
		List<PdfRow> rows = new ArrayList<PdfRow>();

		PdfRow row = new PdfRow(RADIO_FIELD_ROW, RADIO_FIELD_COL);

		String answerText = baseAnswer.getLabel();

		PdfPCell field = PdfPCellGenerator.generateText(answerText);
		// Its parent it's not an answer then they are first level. If not they
		// are subanswers.
		if (!(baseAnswer.getParent() instanceof Answer)) {
			field.setPaddingLeft(PADDING);
			field.setCellEvent(new FormRadioField(writer, question.getComparationId(), baseAnswer.getComparationId(),
					radioGroup, 0));
		} else {
			field.setPaddingLeft(PADDING * 2);
			field.setCellEvent(new FormRadioField(writer, question.getComparationId(), baseAnswer.getComparationId(),
					radioGroup, PADDING));
		}
		field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
		row.addCell(field);
		rows.add(row);

		for (TreeObject subanswer : baseAnswer.getChildren()) {
			List<PdfRow> newRows = generateRadioFieldRows(writer, radioGroup, question, (Answer) subanswer);
			rows.addAll(newRows);
		}

		return rows;
	}

	public static PdfRow generateIsHorizontalBlock(int rowSpan, int colSpan) throws BadBlockException {
		PdfRow row = new PdfRow(rowSpan, colSpan);
		row.addCell(PdfPCellGenerator.generateDescription("Note: Horizontal Layout.", colSpan));
		return row;
	}

	public static PdfRow createTextRow(String description, int textBlockRow, int textBlockCol)
			throws BadBlockException {
		PdfRow row = new PdfRow(textBlockRow, textBlockCol);
		row.addCell(PdfPCellGenerator.generateText(description));
		return row;
	}

	public static PdfRow generateStructureGroupRoot(BaseGroup group) throws BadBlockException {
		PdfRow row = new PdfRow(PdfBlockGenerator.GROUP_ROWS, PdfBlockGenerator.STRUCTURE_COLS);

		PdfPCell cellName = PdfPCellGenerator.generateNameCell(group);
		PdfPCell cellGroupPath = PdfPCellGenerator.generateGroupPathCell(group);

		row.addCell(cellName);
		row.addCell(cellGroupPath);
		return row;
	}

	public static PdfRow generateStructureGroup(BaseGroup group) throws BadBlockException {
		PdfRow row = new PdfRow(PdfBlockGenerator.GROUP_ROWS, PdfBlockGenerator.STRUCTURE_COLS);

		PdfPCell whiteCell = PdfPCellGenerator.generateEmptyCell(1);
		PdfPCell cellName = PdfPCellGenerator.generateNameCell(group);
		PdfPCell cell = new PdfPCell(ParagraphGenerator.generateSmallFontParagraph("(GROUP)"));
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setColspan(2);

		row.addCell(whiteCell);
		row.addCell(cellName);
		row.addCell(cell);
		return row;
	}

	public static PdfRow generateEmptyRow(int rows, int cols) throws BadBlockException {
		PdfRow row = new PdfRow(rows, cols);
		PdfPCell cell = PdfPCellGenerator.generateEmptyCell(cols);
		cell.disableBorderSide(Rectangle.LEFT);
		cell.disableBorderSide(Rectangle.RIGHT);
		cell.disableBorderSide(Rectangle.TOP);
		cell.disableBorderSide(Rectangle.BOTTOM);
		cell.setFixedHeight(10f);
		row.addCell(cell);
		return row;
	}

	public static PdfRow generateTitleRow(String... titles) throws BadBlockException {
		PdfRow row = new PdfRow(MIN_TITLE_ROW, titles.length);
		for (String title : titles) {
			row.addCell(PdfPCellGenerator.generateTitle(title));
		}
		return row;
	}

	public static PdfRow generateBorderlessTitleRow(String... titles) throws BadBlockException {
		PdfRow row = generateTitleRow(titles);
		for (PdfPCell cell : row.getCells()) {
			cell.disableBorderSide(Rectangle.LEFT);
			cell.disableBorderSide(Rectangle.RIGHT);
			cell.disableBorderSide(Rectangle.TOP);
			cell.disableBorderSide(Rectangle.BOTTOM);
		}
		return row;
	}

	public static PdfRow generateVariableRow(CustomVariable variable) throws BadBlockException {
		return generateTitleRow(variable.getName(), variable.getType().toString(), variable.getScope().toString(),
				variable.getDefaultValue());
	}

	public static PdfRow generateRuleRow(TableRuleRow rule) throws BadBlockException {
		PdfRow row = new PdfRow(1, 2);
		row.addCell(PdfPCellGenerator.generateDefaultCell(rule.getConditionsForDrools().getRepresentation()));
		row.addCell(PdfPCellGenerator.generateDefaultCell(rule.getAction().getRepresentation()));
		return row;
	}

}
