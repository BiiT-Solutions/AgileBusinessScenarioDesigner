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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.dao.ISimpleTestScenarioViewDao;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.SimpleTestScenarioView;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class WindowLaunchTestScenario extends AcceptCancelWindow {
	private static final long serialVersionUID = 7743313235048085158L;
	private ISimpleFormViewDao simpleFormViewDao;
	private ISimpleTestScenarioViewDao simpleTestScenarioViewDao;
	private List<SimpleFormView> formData;
	private List<SimpleTestScenarioView> testScenarioData;
	private ComboBox formVersion;
	private ComboBox testScenario;

	public WindowLaunchTestScenario(SimpleFormView formView) {
		// Add Vaadin context to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		simpleFormViewDao = (ISimpleFormViewDao) helper.getBean("simpleFormViewDao");
		simpleTestScenarioViewDao = (ISimpleTestScenarioViewDao) helper.getBean("simpleTestScenarioViewDao");

		setCaption(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_CAPTION));
		setWidth("400px");
		setHeight("200px");
		setResizable(false);
		setContent(generateContent(formView));
	}

	private Component generateContent(SimpleFormView formView) {
		FormLayout layout = new FormLayout();

		formVersion = new ComboBox(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_FORM_LABEL));
		formVersion.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		formVersion.setNullSelectionAllowed(false);
		initializeFormData(formView);
		for (SimpleFormView form : formData) {
			formVersion.addItem(form);
			formVersion.setItemCaption(form, "v" + form.getVersion().toString());
		}

		testScenario = new ComboBox(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_TEST_SCENARIO_LABEL));
		testScenario.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		testScenario.setNullSelectionAllowed(false);
		initializeTestScenarioData(formView);
		for (SimpleTestScenarioView testScenarioView : testScenarioData) {
			testScenario.addItem(testScenarioView);
			testScenario.setItemCaption(testScenarioView,
					testScenarioView.getName() + " (v" + testScenarioView.getFormVersion() + ")");
		}
		if (!testScenarioData.isEmpty()) {
			testScenario.setValue(testScenarioData.get(0));
		}

		formVersion.setValue(formView);
		formVersion.setWidth(100.0f, Unit.PERCENTAGE);
		testScenario.setWidth(100.0f, Unit.PERCENTAGE);
		layout.addComponent(formVersion);
		layout.addComponent(testScenario);
		return layout;
	}

	/**
	 * Loads all the test scenarios related with all the form versions
	 */
	private void initializeTestScenarioData(SimpleFormView formView) {
		if (formView != null) {
			testScenarioData = new ArrayList<>();
			testScenarioData.addAll(simpleTestScenarioViewDao.getSimpleTestScenariosByFormId(formView.getId()));
			// To show first the newer versions
			Collections.reverse(testScenarioData);
		}
	}

	/**
	 * This function loads from database all form elements with the specified name .
	 * At the end it orders each form list by version number.
	 *
	 * @return
	 */
	private void initializeFormData(SimpleFormView formView) {
		if (formView != null) {
			formData = simpleFormViewDao.getSimpleFormViewByLabelAndOrganization(formView.getLabel(),
					formView.getOrganizationId());
		} else {
			formData = new ArrayList<>();
		}
	}

	public Long getSelectedFormId() {
		SimpleFormView simpleFormView = (SimpleFormView) formVersion.getValue();
		if (simpleFormView != null) {
			return simpleFormView.getId();
		} else {
			return null;
		}
	}

	public Long getSelectedTestScenarioId() {
		SimpleTestScenarioView simpleTestScenarioViewView = (SimpleTestScenarioView) testScenario.getValue();
		if (simpleTestScenarioViewView != null) {
			return simpleTestScenarioViewView.getId();
		} else {
			return null;
		}
	}
}
