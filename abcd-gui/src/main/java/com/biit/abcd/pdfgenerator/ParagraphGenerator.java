package com.biit.abcd.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.pdfgenerator.utils.PdfAlign;
import com.biit.abcd.pdfgenerator.utils.PdfFont;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.TreeObject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/**
 * Generate methods for all types of text etiquettes in the form.
 * 
 */
public class ParagraphGenerator {

	private final static float PADDING = 20;

	public static Paragraph generate(String content, PdfFont font, PdfAlign align, float spacing)
			throws DocumentException {
		Paragraph annexTitle = new Paragraph(content, font.getFont());
		annexTitle.setAlignment(align.getAlignment());
		annexTitle.setSpacingAfter(spacing);
		return annexTitle;
	}

	public static void generateAndAddTitle(Document document, String content, PdfAlign align) throws DocumentException {
		document.add(generate(content, PdfFont.FORM_TITLE_FONT, align, PADDING));
	}

	public static void generateAndAddSubtitle(Document document, String content, PdfAlign align)
			throws DocumentException {
		document.add(generate(content, PdfFont.SUBTITLE_FONT, align, PADDING * 2));
	}

	public static void generateAndAddFormTitle(Document document, String content, PdfAlign align)
			throws DocumentException {
		Paragraph title = new Paragraph(new Paragraph(content, PdfFont.FORM_TITLE_FONT.getFont()));
		title.setAlignment(align.getAlignment());
		title.setSpacingAfter(PADDING);
		document.add(title);
	}

	public static void generateAndAddAnnexTitle(Document document, String content, PdfAlign align)
			throws DocumentException {
		document.add(generate(content, PdfFont.ANNEX_TITLE_FONT, align, PADDING));
	}

	public static Paragraph generateLabelParagraph(TreeObject object) {
		return new Paragraph(object.getLabel(), PdfFont.NORMAL_FONT.getFont());
	}

	public static Paragraph generateRepeatableParagraph() {
		return new Paragraph("<Repeatable>", PdfFont.SMALL_FONT.getFont());
	}

	public static Paragraph generateNameParagraph(TreeObject object) {
		if (object instanceof Answer && object.getParent() != null && object.getParent() instanceof Answer) {
			return new Paragraph(" - <" + object.getName() + ">", PdfFont.SMALL_FONT.getFont());
		}
		return new Paragraph("<" + object.getName() + ">", PdfFont.SMALL_FONT.getFont());
	}

	public static Paragraph generateAnswerFormatParagraph(Question question) {
		switch (question.getAnswerType()) {
		case INPUT:
			return new Paragraph("(" + question.getAnswerFormat().toString() + ")", PdfFont.SMALL_FONT.getFont());
		case MULTI_CHECKBOX:
		case RADIO:
			return new Paragraph("(" + question.getAnswerType().toString() + ")", PdfFont.SMALL_FONT.getFont());
		default:
			return new Paragraph("-", PdfFont.SMALL_FONT.getFont());
		}
	}

	public static Phrase generateAnswerSubformatParagraph(Question question) {
		return new Paragraph("-", PdfFont.SMALL_FONT.getFont());
	}

	public static Paragraph generateTextParagraph(String textParagraph) {
		Paragraph paragraph = new Paragraph(textParagraph, PdfFont.NORMAL_FONT.getFont());
		paragraph.setAlignment(PdfAlign.ALIGN_JUSTIFIED.getAlignment());
		return paragraph;
	}

	public static List<Paragraph> generateTextParagraphs(List<String> textParagraphs) {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		for (String text : textParagraphs) {
			paragraphs.add(generateTextParagraph(text));
		}
		return paragraphs;
	}

	public static Paragraph generateFieldName(Question question) {
		String label = question.getAnswerType() != AnswerType.MULTI_CHECKBOX ? question.getLabel() + "*"
				: question.getLabel();
		return new Paragraph(label, PdfFont.NORMAL_FONT.getFont());
	}

	/**
	 * Text for descriptions
	 * 
	 * @param description
	 * @return
	 */
	public static Phrase generateDescription(String description) {
		return new Paragraph("Hint: " + description, PdfFont.DESCRIPTION_FONT.getFont());
	}
}
