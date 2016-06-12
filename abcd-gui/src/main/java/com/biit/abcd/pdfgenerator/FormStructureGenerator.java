package com.biit.abcd.pdfgenerator;

import com.biit.abcd.pdfgenerator.utils.PdfAlign;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.entity.TreeObject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Utility method to generate the form annex pdf.
 *
 */
public class FormStructureGenerator {

	public static void generateAndAdd(Document document, TreeObject treeObject) throws DocumentException {

		ParagraphGenerator.generateAndAddTitle(document, "Form Structure", PdfAlign.ALIGN_CENTER);
		ParagraphGenerator.generateAndAddAnnexTitle(document, "Element List", PdfAlign.ALIGN_CENTER);

		// Generate table
		PdfPTable annexTable = PdfTableGenerator.generateAnnexFormTable((Form) treeObject);

		document.add(annexTable);
	}

}
