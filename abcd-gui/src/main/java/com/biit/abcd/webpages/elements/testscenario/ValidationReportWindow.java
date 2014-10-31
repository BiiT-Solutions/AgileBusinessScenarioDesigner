package com.biit.abcd.webpages.elements.testscenario;

import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;

public class ValidationReportWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 2766182313445284322L;

	protected enum ValidationReportWindowTableProperties {
		ELEMENT_NAME
	}

	public ValidationReportWindow(LanguageCodes caption, List<String> modifications) {
		super();
		setModal(true);
		setWidth("700px");
		setHeight("400px");
		setCaption(ServerTranslate.translate(caption));
		setContent(generateContent(modifications));
	}

	@SuppressWarnings("unchecked")
	private Component generateContent(List<String> modifications) {
		Table modificationsTable = new Table();
		modificationsTable.setSizeFull();
		modificationsTable.addContainerProperty(ValidationReportWindowTableProperties.ELEMENT_NAME, String.class, null,
				ServerTranslate.translate(LanguageCodes.TEST_SCENARIO_VALIDATOR_REPORT_TABLE_HEADER), null, Align.LEFT);
		for (String modification : modifications) {
			Object itemId = modificationsTable.addItem();
			modificationsTable.getItem(itemId).getItemProperty(ValidationReportWindowTableProperties.ELEMENT_NAME)
					.setValue(modification);
		}

		return modificationsTable;
	}
}
