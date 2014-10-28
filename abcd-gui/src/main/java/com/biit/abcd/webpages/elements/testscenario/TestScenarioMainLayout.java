package com.biit.abcd.webpages.elements.testscenario;

import java.util.HashMap;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;

public class TestScenarioMainLayout extends HorizontalLayout {
	private static final long serialVersionUID = -3526986076061463631L;
	private static final String PANEL_STYLE = "v-test-scenario-background-panel";
	private TestScenarioTable treeTestTable;
	private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private Panel editorBackground;

	public TestScenarioMainLayout() {
		super();
		setSizeFull();
		setStyleName(Runo.PANEL_LIGHT);
	}

	public void setContent(Form form, TestScenario testScenario) throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException {
		removeAllComponents();
		if (form != null && testScenario != null) {
			TestScenarioValidator.checkTestScenarioStructure(form, testScenario);
			originalReferenceTreeObjectMap = form.getOriginalReferenceTreeObjectMap();
			createContent(form, testScenario);
		}
	}

	/**
	 * Creates the content for the complete test scenario definition
	 * 
	 * @param form
	 * @param testScenario
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 */
	private void createContent(Form form, TestScenario testScenario) throws NotValidChildException,
			FieldTooLongException, CharacterNotAllowedException {
		if (form != null && testScenario != null) {
			createTreeTable(testScenario);
			createEditForm();
			addComponent(treeTestTable);
			addComponent(editorBackground);
		}
	}

	private void createTreeTable(TestScenario testScenario) {
		treeTestTable = new TestScenarioTable();
		treeTestTable.setSizeFull();
		treeTestTable.setRootElement(testScenario.getTestScenarioForm());
		treeTestTable.setSelectable(true);
		treeTestTable.setNullSelectionAllowed(false);
		treeTestTable.setImmediate(true);
		treeTestTable.setValue(testScenario.getTestScenarioForm());
		treeTestTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1835114202597377993L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Object valueSelected = event.getProperty().getValue();
				if (valueSelected instanceof TestScenarioCategory) {
					editorBackground.setContent(new CustomCategoryEditor(originalReferenceTreeObjectMap,
							(TreeObject) valueSelected));
				} else {
					editorBackground.setContent(null);
				}
			}
		});
	}

	private Component createEditForm() {
		editorBackground = new Panel();
		editorBackground.setSizeFull();
		editorBackground.setStyleName(PANEL_STYLE);
		return editorBackground;
	}

	public TestScenarioTable getTreeTestTable() {
		return treeTestTable;
	}

	public void setTreeTestTable(TestScenarioTable treeTestTable) {
		this.treeTestTable = treeTestTable;
	}

	public void refreshTable(TreeObject treeObject) {
		this.treeTestTable.setRootElement(treeObject);
	}

	// }
	// /**
	// * Creates the map that stores the test scenario objects information
	// */
	// private void createTestScenarioObjectAbsolutePathNameMap() {
	// absolutePathTestScenarioObjectMap = new HashMap<String, TreeObject>();
	// TestScenarioObject testForm = testScenario.getTestScenarioForm();
	// if (testForm != null) {
	// fillAbsolutePathTestScenarioObjectMap(testForm);
	// }
	// }
	//
	// private void fillAbsolutePathTestScenarioObjectMap(TestScenarioObject
	// testScenarioObject) {
	// absolutePathTestScenarioObjectMap.put(testScenarioObject.getAbsoluteGenericPath(),
	// testScenarioObject);
	// for (TreeObject child : testScenarioObject.getChildren()) {
	// fillAbsolutePathTestScenarioObjectMap((TestScenarioObject) child);
	// }
	// }
	//
	// /**
	// * Returns the test scenario object or a new test scenario object if not
	// * found
	// *
	// * @param testScenarioObjectParent
	// * : parent of the object searched
	// * @param absolutePathName
	// * : absolute name path of the object searched
	// * @return
	// * @throws NotValidChildException
	// */
	// private TestScenarioObject getTestScenarioObject(TestScenarioObject
	// testScenarioObjectParent,
	// String absolutePathName, boolean question) throws NotValidChildException
	// {
	// TestScenarioObject testScenarioObject;
	// if (absolutePathTestScenarioObjectMap.containsKey(absolutePathName)) {
	// testScenarioObject = (TestScenarioObject)
	// absolutePathTestScenarioObjectMap.get(absolutePathName);
	// System.out.println("TREE OBJECT RETRIEVED: " +
	// testScenarioObject.getAbsoluteGenericPath());
	// } else {
	// if (question) {
	// testScenarioObject = new TestScenarioQuestionAnswer(absolutePathName);
	// } else {
	// testScenarioObject = new TestScenarioObject(absolutePathName);
	// }
	// try {
	// testScenarioObjectParent.addChild(testScenarioObject);
	// testScenarioObject.setAbsoluteGenericPath(absolutePathName);
	// absolutePathTestScenarioObjectMap.put(absolutePathName,
	// testScenarioObject);
	// System.out.println("TREE OBJECT CREATED: " +
	// testScenarioObject.getAbsoluteGenericPath());
	// } catch (NotValidChildException e) {
	// if (testScenarioObjectParent instanceof
	// TestScenarioRepeatedGroupContainer) {
	// // The user changed the property repeatable of the group in
	// // the form structure
	// // We have to create a new parent
	// testScenarioObjectParent = new
	// TestScenarioObject(testScenarioObjectParent.getAbsoluteGenericPath());
	// testScenarioObjectParent.setXmlTag(testScenarioObjectParent.getXmlTag());
	// absolutePathTestScenarioObjectMap.put(testScenarioObjectParent.getAbsoluteGenericPath(),
	// testScenarioObjectParent);
	// getTestScenarioObject(testScenarioObjectParent, absolutePathName,
	// question);
	// System.out.println("TREE OBJECT MODIFIED: " +
	// testScenarioObjectParent.getAbsoluteGenericPath());
	// }
	// // throw new NotValidChildException(e.getMessage());
	// }
	//
	// }
	// return testScenarioObject;
	// }
}
