package com.biit.abcd.webpages.elements.testscenario;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CustomCategoryEditor extends CustomComponent {
	private static final long serialVersionUID = 3001815791111789912L;
	private static final String CLASSNAME = "v-test-scenario-category-editor";
	private static final String HEADER_STYLE_NAME = "v-test-scenario-category-header-editor";
	private Label categoryName;
	private FormLayout editorLayout;
	private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private TestScenarioCategory testScenarioCategory;

	public CustomCategoryEditor(HashMap<String, TreeObject> originalReferenceTreeObjectMap,
			TreeObject testScenarioObject) {
		this.originalReferenceTreeObjectMap = originalReferenceTreeObjectMap;
		testScenarioCategory = (TestScenarioCategory) testScenarioObject;
		setCompositionRoot(generateContent(testScenarioObject));
		setStyleName(CLASSNAME);
		setContent(testScenarioObject);
	}

	private Component generateContent(TreeObject treeObject) {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		categoryName = new Label(treeObject.getName());
		categoryName.setWidth("100%");
		categoryName.setStyleName(HEADER_STYLE_NAME);

		editorLayout = new FormLayout();
		editorLayout.setWidth("100%");
		editorLayout.setHeight(null);
		editorLayout.setMargin(new MarginInfo(false, true, true, true));

		rootLayout.addComponent(categoryName);
		rootLayout.addComponent(editorLayout);

		return rootLayout;
	}

	public void setContent(TreeObject testScenarioObject) {
		List<TreeObject> questions = testScenarioObject.getChildren(TestScenarioQuestion.class);
		if ((questions != null) && !questions.isEmpty()) {
			// Add the questions of the category
			addEditor(new CustomQuestionEditor(originalReferenceTreeObjectMap, questions));
		}
		// Add the groups of the category
		List<TreeObject> testScenarioGroups = testScenarioObject.getChildren(TestScenarioGroup.class);
		if ((testScenarioGroups != null) && !testScenarioGroups.isEmpty()) {
			for (TreeObject testScenarioGroup : testScenarioGroups) {
				CustomGroupEditor customGroupEditor = new CustomGroupEditor(originalReferenceTreeObjectMap,
						testScenarioGroup);
				addEditor(customGroupEditor);
				setGroupButtonsListeners(customGroupEditor);
			}
		}
	}

	private void setGroupButtonsListeners(final CustomGroupEditor customGroupEditor) {
		customGroupEditor.addCopyRepeatableGroupButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 7746273894922691271L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TestScenarioGroup newTestScenarioGroup = customGroupEditor.getTestScenarioGroup()
							.copyTestScenarioGroup();
					getTestScenarioCategory().addChild(newTestScenarioGroup);
					CustomGroupEditor newCustomGroupEditor = new CustomGroupEditor(originalReferenceTreeObjectMap,
							newTestScenarioGroup);
					addEditor(newCustomGroupEditor);
					setGroupButtonsListeners(newCustomGroupEditor);
					customGroupEditor.setAddGroupButtonEnable(false);
					customGroupEditor.setRemoveGroupButtonEnable(true);
				} catch (NotValidChildException | FieldTooLongException | CharacterNotAllowedException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});

		customGroupEditor.addRemoveRepeatableGroupButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2921945959002763631L;

			@Override
			public void buttonClick(ClickEvent event) {
				TreeObject testScenarioGroup = customGroupEditor.getTestScenarioGroup();
				try {
					testScenarioGroup.remove();
					removeEditor(customGroupEditor);

					Iterator<Component> iterator = editorLayout.iterator();
					while (iterator.hasNext()) {
						Component nextComponent = iterator.next();
						if (nextComponent instanceof CustomGroupEditor) {
							CustomGroupEditor groupEditor = (CustomGroupEditor) nextComponent;
							groupEditor.enableDisableAddRemoveButton(groupEditor.getTestScenarioGroup());
						}
					}

				} catch (DependencyExistException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

	public void addEditor(CustomComponent groupEditor) {
		editorLayout.addComponent(groupEditor);
	}

	public void removeEditor(CustomComponent groupEditor) {
		editorLayout.removeComponent(groupEditor);
	}

	public TreeObject getTestScenarioCategory() {
		return testScenarioCategory;
	}
}
