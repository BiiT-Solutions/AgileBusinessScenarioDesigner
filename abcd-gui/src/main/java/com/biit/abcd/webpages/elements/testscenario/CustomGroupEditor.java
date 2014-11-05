package com.biit.abcd.webpages.elements.testscenario;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class CustomGroupEditor extends CustomComponent {
	private static final long serialVersionUID = 3099517634702528173L;
	private static final String CLASSNAME = "v-test-scenario-group-editor";
	private static final String HEADER_STYLE_NAME = "v-test-scenario-group-header-editor";
	private FormLayout editorLayout;
	private HorizontalLayout groupButtonsLayout;
	private Button copyRepeatableGroup, removeRepeatableGroup;
	private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private TestScenarioGroup testScenarioGroup;

	public CustomGroupEditor(HashMap<String, TreeObject> originalReferenceTreeObjectMap, TreeObject testScenarioObject) {
		this.originalReferenceTreeObjectMap = originalReferenceTreeObjectMap;
		testScenarioGroup = (TestScenarioGroup) testScenarioObject;
		setCompositionRoot(generateContent());
		setStyleName(CLASSNAME);
		setContent(testScenarioObject);
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		groupButtonsLayout = new HorizontalLayout();
		groupButtonsLayout.setHeight("25px");
		groupButtonsLayout.setWidth("100%");
		groupButtonsLayout.setMargin(new MarginInfo(false, true, false, false));
		groupButtonsLayout.setStyleName(HEADER_STYLE_NAME);

		editorLayout = new FormLayout();
		editorLayout.setWidth("100%");
		editorLayout.setHeight(null);
		editorLayout.setMargin(new MarginInfo(false, true, true, true));

		rootLayout.addComponent(groupButtonsLayout);
		rootLayout.addComponent(editorLayout);

		return rootLayout;
	}

	public void setContent(TreeObject testScenarioObject) {
		createGroupHeader(testScenarioObject);

		List<TreeObject> questions = testScenarioObject.getChildren(TestScenarioQuestion.class);
		if ((questions != null) && !questions.isEmpty()) {
			// Add the questions of the group
			addEditor(new CustomQuestionEditor(originalReferenceTreeObjectMap, questions));
		}
		// Add the groups of the group (if any)
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

	public void addEditor(CustomComponent groupEditor) {
		editorLayout.addComponent(groupEditor);
	}

	public void addEditor(CustomComponent groupEditor, int index) {
		editorLayout.addComponent(groupEditor, index);
	}

	public void removeEditor(CustomComponent groupEditor) {
		editorLayout.removeComponent(groupEditor);
	}

	private void createGroupHeader(TreeObject testScenarioObject) {
		Label groupName = new Label(testScenarioObject.getName());
		groupName.setWidth("100%");
		groupButtonsLayout.addComponent(groupName);

		TreeObject treeObject = originalReferenceTreeObjectMap.get(testScenarioObject.getOriginalReference());

		if ((treeObject instanceof Group) && (((Group) treeObject).isRepeatable())) {
			copyRepeatableGroup = new Button("+");
			removeRepeatableGroup = new Button("x");
			groupButtonsLayout.addComponent(copyRepeatableGroup);
			groupButtonsLayout.addComponent(removeRepeatableGroup);
			groupButtonsLayout.setComponentAlignment(copyRepeatableGroup, Alignment.MIDDLE_RIGHT);
			groupButtonsLayout.setComponentAlignment(removeRepeatableGroup, Alignment.MIDDLE_RIGHT);
			if (!getTestScenarioGroup().isAddEnabled()) {
				setAddGroupButtonEnable(false);
			}
			enableDisableAddRemoveButton(testScenarioObject);
		}
		groupButtonsLayout.setExpandRatio(groupName, 1);
	}

	public void addCopyRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (copyRepeatableGroup != null) {
			copyRepeatableGroup.addClickListener(listener);
		}
	}

	public void removeCopyRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (copyRepeatableGroup != null) {
			copyRepeatableGroup.removeClickListener(listener);
		}
	}

	public void addRemoveRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (removeRepeatableGroup != null) {
			removeRepeatableGroup.addClickListener(listener);
		}
	}

	public void removeRemoveRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (removeRepeatableGroup != null) {
			removeRepeatableGroup.removeClickListener(listener);
		}
	}

	public TestScenarioGroup getTestScenarioGroup() {
		return testScenarioGroup;
	}

	public void setAddGroupButtonEnable(boolean enable) {
		if (copyRepeatableGroup != null) {
			copyRepeatableGroup.setEnabled(enable);
		}
	}

	public void setRemoveGroupButtonEnable(boolean enable) {
		if (removeRepeatableGroup != null) {
			removeRepeatableGroup.setEnabled(enable);
		}
	}

	private void setGroupButtonsListeners(final CustomGroupEditor customGroupEditor) {
		customGroupEditor.addCopyRepeatableGroupButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 7746273894922691271L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					TestScenarioGroup newTestScenarioGroup = customGroupEditor.getTestScenarioGroup()
							.copyTestScenarioGroup(false);

					Integer childIndex = getTestScenarioGroup().getIndex(customGroupEditor.getTestScenarioGroup());
					getTestScenarioGroup().addChild(childIndex + 1, newTestScenarioGroup);

					CustomGroupEditor newCustomGroupEditor = new CustomGroupEditor(originalReferenceTreeObjectMap,
							newTestScenarioGroup);
					addEditor(newCustomGroupEditor, childIndex + 1);
					setGroupButtonsListeners(newCustomGroupEditor);
					customGroupEditor.setAddGroupButtonEnable(false);
					customGroupEditor.getTestScenarioGroup().setAddEnabled(false);
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

	/**
	 * If the group is the last repeatable group the remove button is disabled
	 * 
	 * @param testScenarioGroup
	 */
	public void enableDisableAddRemoveButton(TreeObject testScenarioGroup) {
		TreeObject treeObject = originalReferenceTreeObjectMap.get(testScenarioGroup.getOriginalReference());
		if ((treeObject instanceof Group) && ((Group) treeObject).isRepeatable()) {
			TreeObject parent = testScenarioGroup.getParent();
			Set<TreeObject> elementsToDelete = parent.getElementsToDelete();
			List<TreeObject> groups = parent.getChildren(TestScenarioGroup.class);
			int repeatedGroups = 0;
			for (TreeObject group : groups) {
				// Repeated groups have the same name at the same level
				// The group shouldn't be in the list to delete
				if (group.getName().equals(testScenarioGroup.getName()) && !elementsToDelete.contains(group)) {
					repeatedGroups++;
				}
			}
			if (repeatedGroups == 1) {
				setAddGroupButtonEnable(true);
				getTestScenarioGroup().setAddEnabled(true);
				setRemoveGroupButtonEnable(false);
			} else {
				setRemoveGroupButtonEnable(true);
			}
		}
	}
}
