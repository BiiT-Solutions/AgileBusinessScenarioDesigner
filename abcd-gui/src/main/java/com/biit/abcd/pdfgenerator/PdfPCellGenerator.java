package com.biit.abcd.pdfgenerator;

import java.util.List;

import com.biit.abcd.pdfgenerator.utils.PdfFont;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.TreeObject;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Utility class to generate PdfPCells from different elements of the form.
 *
 */
public class PdfPCellGenerator {

	private final static int BORDER = Rectangle.NO_BORDER;

	public static PdfPCell generateEmptyCell(int colspan) {
		PdfPCell cell = new PdfPCell();
		cell.setColspan(colspan);
		return cell;
	}

	public static PdfPCell generateLabelCell(TreeObject object) {
		return new PdfPCell(ParagraphGenerator.generateLabelParagraph(object));
	}

	public static PdfPCell generateNameCell(TreeObject object) {
		return new PdfPCell(ParagraphGenerator.generateNameParagraph(object));
	}

	public static PdfPCell generateAnswerFormatParagraph(Question question) {
		PdfPCell cell = new PdfPCell(ParagraphGenerator.generateAnswerFormatParagraph(question));
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		return cell;
	}

	public static PdfPCell generateAnswerSubformatParagraph(Question question) {
		PdfPCell cell = new PdfPCell(ParagraphGenerator.generateAnswerSubformatParagraph(question));
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		return cell;
	}

	public static PdfPCell generateInputFieldCell(PdfWriter writer, Question question) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(BORDER);
		if (question.getAnswerType() == AnswerType.INPUT) {
			cell.setCellEvent(new FormTextField(writer, question.getComparationId()));
		} else {
			FormTextArea textArea = new FormTextArea(writer, question.getComparationId());
			cell.setCellEvent(textArea);
			cell.setFixedHeight(textArea.getHeight());
		}
		return cell;
	}

	public static PdfPCell generateFormQuestionNameCell(Question question) {
		PdfPCell nameCell = new PdfPCell(ParagraphGenerator.generateFieldName(question));
		nameCell.setBorder(BORDER);
		return nameCell;
	}

	public static PdfPCell generateText(String text) {
		PdfPCell labelCell = new PdfPCell(new Phrase(text));
		labelCell.setBorder(BORDER);
		return labelCell;
	}

	public static PdfPCell generateDescription(String description, int span) {
		PdfPCell cell = new PdfPCell(ParagraphGenerator.generateDescription(description));
		cell.setColspan(span);
		cell.setBorder(BORDER);
		cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_TOP);
		return cell;
	}

	public static PdfPCell generateGroupPathCell(BaseGroup group) {
		List<String> pathElements = group.getPath();
		String path = "/";
		for (int i = 0; i < pathElements.size() - 1; i++) {
			path += pathElements.get(i);
		}
		PdfPCell cell = new PdfPCell(ParagraphGenerator.generateTextParagraph(path));
		cell.setColspan(PdfBlockGenerator.STRUCTURE_COLS - 1);
		return cell;
	}

	public static PdfPCell generateTableTitle(String title) {
		// return generateDefaultCell(title);
		return new PdfPCell(new Paragraph(title, PdfFont.TABLE_TITLE_FONT.getFont()));
	}

	public static PdfPCell generateDefaultCell(String text) {
		PdfPCell labelCell = new PdfPCell(new Paragraph(text, PdfFont.NORMAL_FONT.getFont()));
		return labelCell;
	}

	public static PdfPCell generateHeaderTitle(String title) {
		PdfPCell cell = new PdfPCell(new Paragraph(title, PdfFont.HEADER_FONT.getFont()));
		cell.setBorder(BORDER);
		return cell;
	}

}
