package com.biit.abcd.pdfgenerator;

import com.biit.abcd.pdfgenerator.utils.PdfAlign;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;
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
		ParagraphGenerator.generateAndAddAnnexTitle(document, "Element List", PdfAlign.ALIGN_CENTER);
		document.add(PdfTableGenerator.generateStructureFormTable((Form) treeObject));
		document.newPage();
		ParagraphGenerator.generateAndAddTitle(document, "Form Variables", PdfAlign.ALIGN_CENTER);
		document.add(PdfTableGenerator.generateFormVariablesTable((Form) treeObject));
		document.newPage();
		ParagraphGenerator.generateAndAddTitle(document, "Expressions", PdfAlign.ALIGN_CENTER);
		document.add(PdfTableGenerator.generateExpressionsTable((Form) treeObject));
		document.newPage();
		ParagraphGenerator.generateAndAddTitle(document, "Rules", PdfAlign.ALIGN_CENTER);
		document.add(PdfTableGenerator.generateRulesTable((Form) treeObject));
		document.newPage();
		ParagraphGenerator.generateAndAddTitle(document, "Table Rules", PdfAlign.ALIGN_CENTER);
		for (TableRule table : ((Form) treeObject).getTableRules()) {
			document.add(PdfTableGenerator.generateRuleTableTable(table));
		}
	}

}
