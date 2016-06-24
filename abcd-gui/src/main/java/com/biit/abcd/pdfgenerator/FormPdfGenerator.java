package com.biit.abcd.pdfgenerator;

import com.biit.abcd.pdfgenerator.utils.PdfAlign;
import com.biit.abcd.persistence.entity.Form;
import com.biit.utils.date.DateManager;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * iText pdf generator for the form design data. It contains all the functions
 * to generate the general sections of a form.
 * 
 */
public class FormPdfGenerator extends DocumentGenerator {

	private Form form;

	public FormPdfGenerator(Form form) {
		this.form = form;

		FormPageEvent formPageEvent = new FormPageEvent();
		formPageEvent.setHeader(form.getLabel());

		setPageEvent(formPageEvent);
	}

	@Override
	protected void generateDocumentContent(Document document) throws DocumentException {

		ParagraphGenerator.generateAndAddFormTitle(document, form.getLabel(), PdfAlign.ALIGN_CENTER);
		String updateDate = DateManager.convertDateToString(form.getUpdateTime());
		ParagraphGenerator.generateAndAddSubtitle(document, "Version " + form.getVersion() + " (" + updateDate + ")", PdfAlign.ALIGN_CENTER);

		document.newPage();

		// Annex page generation
		FormStructureGenerator.generateAndAdd(document, form);

	}

}
