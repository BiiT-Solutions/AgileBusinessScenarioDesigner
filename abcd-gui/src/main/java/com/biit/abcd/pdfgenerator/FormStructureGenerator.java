package com.biit.abcd.pdfgenerator;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.pdfgenerator.utils.PdfAlign;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.form.entity.TreeObject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * Utility method to generate the form annex pdf.
 *
 */
public class FormStructureGenerator {

	private static final float WHITE_LINE_SPACING = 20.0f;

	public static void generateAndAdd(Document document, TreeObject treeObject) throws DocumentException {
		ParagraphGenerator.generateAndAddTitle(document, "Form Structure", PdfAlign.ALIGN_CENTER);
		// ParagraphGenerator.generateAndAddAnnexTitle(document, "Element List",
		// PdfAlign.ALIGN_CENTER);
		document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
		document.add(PdfTableGenerator.generateStructureFormTable((Form) treeObject));
		document.newPage();

		ParagraphGenerator.generateAndAddTitle(document, "Form Variables", PdfAlign.ALIGN_CENTER);
		document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
		document.add(PdfTableGenerator.generateFormVariablesTable((Form) treeObject));
		document.newPage();

		ParagraphGenerator.generateAndAddTitle(document, "Diagrams", PdfAlign.ALIGN_CENTER);
		document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
		for (Diagram diagram : ((Form) treeObject).getDiagrams()) {
			document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
			//ParagraphGenerator.generateAndAddAnnexTitle(document, diagram.getName(), PdfAlign.ALIGN_CENTER);
			document.add(PdfTableGenerator.generateDiagrams(document, (Form) treeObject, diagram));
			//document.newPage();
		}
		document.newPage();
		
		ParagraphGenerator.generateAndAddTitle(document, "Expressions", PdfAlign.ALIGN_CENTER);
		document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
		document.add(PdfTableGenerator.generateExpressionsTable((Form) treeObject));
		document.newPage();

		ParagraphGenerator.generateAndAddTitle(document, "Rules", PdfAlign.ALIGN_CENTER);
		document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
		document.add(PdfTableGenerator.generateRulesTable((Form) treeObject));
		document.newPage();

		ParagraphGenerator.generateAndAddTitle(document, "Table Rules", PdfAlign.ALIGN_CENTER);
		for (TableRule table : ((Form) treeObject).getTableRules()) {
			document.add(ParagraphGenerator.generateWhiteLine(WHITE_LINE_SPACING));
			ParagraphGenerator.generateAndAddAnnexTitle(document, table.getName(), PdfAlign.ALIGN_CENTER);
			document.add(PdfTableGenerator.generateRuleTableTable(table));
		}
	}

}
