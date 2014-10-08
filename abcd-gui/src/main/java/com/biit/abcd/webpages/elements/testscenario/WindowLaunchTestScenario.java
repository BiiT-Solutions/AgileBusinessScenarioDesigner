package com.biit.abcd.webpages.elements.testscenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	public WindowLaunchTestScenario(String formName) {
		// Add Vaadin context to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		simpleFormViewDao = (ISimpleFormViewDao) helper.getBean("simpleFormViewDao");
		simpleTestScenarioViewDao = (ISimpleTestScenarioViewDao) helper.getBean("simpleTestScenarioViewDao");

		setCaption(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_CAPTION));
		setWidth("20%");
		setHeight("20%");
		setClosable(false);
		setModal(true);
		setResizable(false);
		setContent(generateContent(formName));
	}

	private Component generateContent(String formName) {
		FormLayout layout = new FormLayout();

		formVersion = new ComboBox(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_FORM_LABEL));
		formVersion.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		formVersion.setNullSelectionAllowed(false);
		initializeFormData(formName);
		for (SimpleFormView formView : formData) {
			formVersion.addItem(formView);
			formVersion.setItemCaption(formView, "v" + formView.getVersion().toString());
		}

		testScenario = new ComboBox(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_TEST_SCENARIO_LABEL));
		testScenario.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		testScenario.setNullSelectionAllowed(false);
		initializeTestScenarioData();
		for (SimpleTestScenarioView testScenarioView : testScenarioData) {
			testScenario.addItem(testScenarioView);
			testScenario.setItemCaption(testScenarioView, testScenarioView.getName());
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
	private void initializeTestScenarioData() {
		testScenarioData = new ArrayList<SimpleTestScenarioView>();
		for (SimpleFormView formView : formData) {
			testScenarioData.addAll(simpleTestScenarioViewDao.getSimpleTestScenarioByFormId(formView.getId()));
		}
	}

	/**
	 * This function loads from database all form elements with the specified
	 * name . At the end it orders each form list by version number.
	 * 
	 * @return
	 * @throws NotConnectedToDatabaseException
	 */
	private void initializeFormData(String formName) {
		formData = simpleFormViewDao.getSimpleFormViewByName(formName);
		Collections.sort(formData, new FormVersionComparator());
	}

	/**
	 * This is a form comparator that sorts by version number. It is used to
	 * sort the lists of forms
	 * 
	 */
	private class FormVersionComparator implements Comparator<SimpleFormView> {
		@Override
		public int compare(SimpleFormView arg0, SimpleFormView arg1) {
			return arg0.getVersion().compareTo(arg1.getVersion());
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