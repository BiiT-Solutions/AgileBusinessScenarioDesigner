package com.biit.abcd.webpages.elements.testscenario;

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
		testScenarioData = new ArrayList<SimpleTestScenarioView>();
		testScenarioData.addAll(simpleTestScenarioViewDao.getSimpleTestScenariosByFormId(formView.getId()));
		// To show first the newer versions
		Collections.reverse(testScenarioData);
	}

	/**
	 * This function loads from database all form elements with the specified
	 * name . At the end it orders each form list by version number.
	 * 
	 * @return
	 * @throws NotConnectedToDatabaseException
	 */
	private void initializeFormData(SimpleFormView formView) {
		formData = simpleFormViewDao.getSimpleFormViewByLabelAndOrganization(formView.getLabel(),
				formView.getOrganizationId());
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