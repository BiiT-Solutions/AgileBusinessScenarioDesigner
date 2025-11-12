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
