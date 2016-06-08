package com.biit.abcd.pdfgenerator;

import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.TreeObject;
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

	public static PdfPCell generateLabelCell(TreeObject object) {
		return new PdfPCell(ParagraphGenerator.generateLabelParagraph(object));
	}

	public static PdfPCell generateNameCell(TreeObject object) {
		return new PdfPCell(ParagraphGenerator.generateNameParagraph(object));
	}

	public static PdfPCell generateAnswerFormatParagraph(Question question) {
		return new PdfPCell(ParagraphGenerator.generateAnswerFormatParagraph(question));
	}

	public static PdfPCell generateAnswerSubformatParagraph(Question question) {
		return new PdfPCell(ParagraphGenerator.generateAnswerSubformatParagraph(question));
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

	public static PdfPCell generateText(String text, int span) {
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

}
