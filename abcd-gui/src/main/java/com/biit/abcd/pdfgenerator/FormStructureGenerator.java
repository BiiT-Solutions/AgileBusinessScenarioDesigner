package com.biit.abcd.pdfgenerator;

import com.biit.abcd.pdfgenerator.utils.PdfAlign;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.entity.TreeObject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * Utility method to generate the form annex pdf.
 *
 */
public class FormStructureGenerator {

	public static void generateAndAdd(Document document, TreeObject treeObject) throws DocumentException {

		ParagraphGenerator.generateAndAddTitle(document, "Form Structure", PdfAlign.ALIGN_CENTER);
		document.newPage();
		ParagraphGenerator.generateAndAddAnnexTitle(document, "Element List", PdfAlign.ALIGN_CENTER);
		document.add(PdfTableGenerator.generateAnnexFormTable((Form) treeObject));
		document.newPage();
		document.add(PdfTableGenerator.generateAnnexFormTable((Form) treeObject));

	}

}
