package com.biit.abcd.webpages.elements.testscenario;

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

import java.util.List;

import com.biit.abcd.core.testscenarios.validator.TestScenarioValidatorMessage;
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

	public ValidationReportWindow(LanguageCodes caption, List<TestScenarioValidatorMessage> testScenarioModifications) {
		super();
		setModal(true);
		setResizable(false);
		setClosable(false);
		setWidth("700px");
		setHeight("400px");
		setCaption(ServerTranslate.translate(caption));
		setContent(generateContent(testScenarioModifications));
	}

	@SuppressWarnings("unchecked")
	private Component generateContent(List<TestScenarioValidatorMessage> testScenarioModifications) {
		Table modificationsTable = new Table();
		modificationsTable.setSizeFull();
		modificationsTable.addContainerProperty(ValidationReportWindowTableProperties.ELEMENT_NAME, String.class, null,
				ServerTranslate.translate(LanguageCodes.TEST_SCENARIO_VALIDATOR_REPORT_TABLE_HEADER), null, Align.LEFT);
		for (TestScenarioValidatorMessage testScenarioModification : testScenarioModifications) {
			Object itemId = modificationsTable.addItem();
			modificationsTable
					.getItem(itemId)
					.getItemProperty(ValidationReportWindowTableProperties.ELEMENT_NAME)
					.setValue(
							ServerTranslate.translate(testScenarioModification.getCode(),
									testScenarioModification.getParameters().toArray()));
		}

		return modificationsTable;
	}
}
