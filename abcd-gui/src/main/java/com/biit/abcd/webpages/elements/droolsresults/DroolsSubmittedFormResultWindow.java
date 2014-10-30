package com.biit.abcd.webpages.elements.droolsresults;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.SubmittedCategory;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
import com.biit.abcd.core.drools.facts.inputform.SubmmitedGroup;
import com.biit.abcd.core.drools.facts.inputform.interfaces.IDroolsTableElement;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

public class DroolsSubmittedFormResultWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -9123887739972604746L;
	private HashMap<CustomVariableScope, List<String>> customVariablesScopeMap;
	private DroolsTreeObjectTable formTreeTable;
	private Form form;
	private List<ElementsToDuplicate> elementsToDuplicate;

	protected enum TreeObjectTableProperties {
		ORIGINAL_VALUE
	}

	/**
	 * Used for the test scenarios of categories
	 * 
	 * @param submittedForm
	 * @param form
	 * @throws CategoryDoesNotExistException
	 * @throws GroupDoesNotExistException
	 */
	public DroolsSubmittedFormResultWindow(ISubmittedForm submittedForm, Form form)
			throws CategoryDoesNotExistException, GroupDoesNotExistException {
		super();
		setCaption("Submitted form scores");
		setWidth("60%");
		setHeight("60%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		formTreeTable = new DroolsTreeObjectTable();
		formTreeTable.setSizeFull();
		formTreeTable.setSelectable(true);
		formTreeTable.setImmediate(true);
		if (submittedForm != null) {
			this.form = form;
			elementsToDuplicate = new ArrayList<ElementsToDuplicate>();
			adaptFormToRepeatableGroups(submittedForm);
			if (!elementsToDuplicate.isEmpty()) {
				duplicateRepeatedGroups();
			}
			formTreeTable.setRootElement(this.form);
			generateContent((SubmittedForm) submittedForm);
		}
		setContent(formTreeTable);
	}

	public DroolsSubmittedFormResultWindow(ISubmittedForm submittedForm) throws CategoryDoesNotExistException,
			GroupDoesNotExistException {
		super();
		setCaption("Submitted form scores");
		setWidth("60%");
		setHeight("60%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		formTreeTable = new DroolsTreeObjectTable();
		formTreeTable.setSizeFull();
		formTreeTable.setSelectable(true);
		formTreeTable.setImmediate(true);
		formTreeTable.setRootElement((IDroolsTableElement) submittedForm);
		generateContent((SubmittedForm) submittedForm);
		setContent(formTreeTable);
	}

	private void generateContent(SubmittedForm submittedForm) {
		formTreeTable.addContainerProperty(TreeObjectTableProperties.ORIGINAL_VALUE, String.class, "");
		formTreeTable.setColumnWidth(TreeObjectTableProperties.ORIGINAL_VALUE, 150);

		if (form != null) {
			List<CustomVariable> sortedCustomVariables = new ArrayList<CustomVariable>();
			sortedCustomVariables.addAll(form.getCustomVariables(com.biit.abcd.persistence.entity.Question.class));
			sortedCustomVariables.addAll(form.getCustomVariables(com.biit.abcd.persistence.entity.Group.class));
			sortedCustomVariables.addAll(form.getCustomVariables(com.biit.abcd.persistence.entity.Category.class));
			sortedCustomVariables.addAll(form.getCustomVariables(com.biit.abcd.persistence.entity.Form.class));

			if ((sortedCustomVariables != null) && (!sortedCustomVariables.isEmpty())) {
				customVariablesScopeMap = new HashMap<CustomVariableScope, List<String>>();
				for (CustomVariable customVariable : sortedCustomVariables) {
					formTreeTable.addContainerProperty(customVariable.getName(), String.class, null);
					formTreeTable.setColumnWidth(customVariable.getName(), 150);
					if (customVariablesScopeMap.get(customVariable.getScope()) == null) {
						List<String> customVariablesNames = new ArrayList<String>();
						customVariablesNames.add(customVariable.getName());
						customVariablesScopeMap.put(customVariable.getScope(), customVariablesNames);
					} else {
						List<String> customVariablesNames = customVariablesScopeMap.get(customVariable.getScope());
						customVariablesNames.add(customVariable.getName());
						customVariablesScopeMap.put(customVariable.getScope(), customVariablesNames);
					}
				}
			}
			// Put form variables
			if ((form != null) && (submittedForm != null)) {
				createFormVariables(form, submittedForm);
				// Put category variables
				List<TreeObject> categories = form.getChildren();
				if (categories != null) {
					for (TreeObject category : categories) {
						// Get the subform category
						SubmittedCategory categorySubForm = getSubmittedFormCategory(category, submittedForm);
						createCategoryVariables(category, categorySubForm);
						// Put category children variables
						List<TreeObject> categoryChildren = category.getChildren();
						if (categoryChildren != null) {
							TreeObject repeatedGroup = null;
							int repeatedGroupIndex = 0;
							for (TreeObject categoryChild : categoryChildren) {
								if (categoryChild instanceof com.biit.abcd.persistence.entity.Group) {
									com.biit.abcd.persistence.entity.Group formGroup = (com.biit.abcd.persistence.entity.Group) categoryChild;
									if (formGroup.isRepeatable()) {
										if (repeatedGroup != null) {
											if (repeatedGroup.getName().equals(formGroup.getName())) {
												repeatedGroupIndex++;
											} else {
												repeatedGroup = formGroup;
												repeatedGroupIndex = 0;
											}
										} else {
											repeatedGroup = formGroup;
										}
										createGroupVariables(
												categoryChild,
												getSubmittedFormRepeatableGroup(categoryChild, categorySubForm,
														repeatedGroupIndex));

									} else {
										createGroupVariables(categoryChild,
												getSubmittedFormGroup(categoryChild, categorySubForm));
									}
								} else if (categoryChild instanceof com.biit.abcd.persistence.entity.Question) {
									createQuestionVariables(categoryChild,
											getSubmittedFormIGroupQuestion(categoryChild, categorySubForm));
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createFormVariables(Form form, SubmittedForm submittedForm) {
		if (customVariablesScopeMap != null) {
			List<String> formVariables = customVariablesScopeMap.get(CustomVariableScope.FORM);
			if (formVariables != null) {
				for (String variable : formVariables) {
					if (submittedForm.getVariableValue(variable) != null) {
						formTreeTable.getItem(form).getItemProperty(variable)
								.setValue(submittedForm.getVariableValue(variable).toString());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createCategoryVariables(TreeObject category, SubmittedCategory categorySubForm) {
		if (customVariablesScopeMap != null) {
			List<String> categoryVariables = customVariablesScopeMap.get(CustomVariableScope.CATEGORY);
			if ((categoryVariables != null) && (categorySubForm != null)) {
				for (String variable : categoryVariables) {
					if (categorySubForm.getVariableValue(variable) != null) {
						formTreeTable.getItem(category).getItemProperty(variable)
								.setValue(categorySubForm.getVariableValue(variable).toString());
					} else {
						formTreeTable.getItem(category).getItemProperty(variable).setValue("-");
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createGroupVariables(TreeObject group, IGroup groupSubForm) {
		if (customVariablesScopeMap != null) {
			List<String> groupVariables = customVariablesScopeMap.get(CustomVariableScope.GROUP);

			if ((groupVariables != null) && (groupSubForm != null)) {
				for (String variable : groupVariables) {
					if (((SubmmitedGroup) groupSubForm).getVariableValue(variable) != null) {
						formTreeTable.getItem(group).getItemProperty(variable)
								.setValue(((SubmmitedGroup) groupSubForm).getVariableValue(variable).toString());
					} else {
						formTreeTable.getItem(group).getItemProperty(variable).setValue("-");
					}
				}
			}
		}
		// Manage the nested values
		List<TreeObject> groupChildren = group.getChildren();
		if (groupChildren != null) {
			TreeObject repeatedGroup = null;
			int repeatedGroupIndex = 0;
			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof com.biit.abcd.persistence.entity.Group) {
					com.biit.abcd.persistence.entity.Group formGroup = (com.biit.abcd.persistence.entity.Group) groupChild;
					if (formGroup.isRepeatable()) {
						if (repeatedGroup != null) {
							if (repeatedGroup.getName().equals(formGroup.getName())) {
								repeatedGroupIndex++;
							} else {
								repeatedGroup = formGroup;
								repeatedGroupIndex = 0;
							}
						} else {
							repeatedGroup = formGroup;
						}
						createGroupVariables(groupChild,
								getSubmittedFormRepeatableGroup(groupChild, groupSubForm, repeatedGroupIndex));
					} else {
						createGroupVariables(groupChild, getSubmittedFormGroup(groupChild, groupSubForm));
					}

				} else if (groupChild instanceof com.biit.abcd.persistence.entity.Question) {
					createQuestionVariables(groupChild, getSubmittedFormIGroupQuestion(groupChild, groupSubForm));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createQuestionVariables(TreeObject question, SubmittedQuestion questionSubForm) {
		if ((questionSubForm != null) && (question != null)) {
			// Set the original value of the question
			if (question instanceof com.biit.abcd.persistence.entity.Question) {
				com.biit.abcd.persistence.entity.Question questionTreeObject = (com.biit.abcd.persistence.entity.Question) question;
				if (questionTreeObject.getAnswerType().equals(AnswerType.INPUT)
						&& questionTreeObject.getAnswerFormat().equals(AnswerFormat.DATE)) {
					try {
						if ((questionSubForm.getAnswer() != null) && (questionSubForm.getAnswer() != "")) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							String formattedDate = dateFormat.format(questionSubForm.getAnswer());
							formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
									.setValue(formattedDate);
						} else {
							formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
									.setValue("-");
						}
					} catch (IllegalArgumentException e) {
						AbcdLogger.errorMessage(this.getClass().getName(), e);
					}
				} else {
					if ((questionSubForm.getAnswer() != null) && (questionSubForm.getAnswer() != "")) {
						formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
								.setValue(questionSubForm.getAnswer().toString());
					} else {
						formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
								.setValue("-");
					}
				}
			}
			if (customVariablesScopeMap != null) {
				List<String> questionVariables = customVariablesScopeMap.get(CustomVariableScope.QUESTION);
				// Set the variable value of the question
				if ((questionVariables != null)) {
					for (String variable : questionVariables) {
						if (questionSubForm.getVariableValue(variable) != null) {
							formTreeTable.getItem(question).getItemProperty(variable)
									.setValue(questionSubForm.getVariableValue(variable).toString());
						} else {
							formTreeTable.getItem(question).getItemProperty(variable).setValue("-");
						}
					}
				}
			}
		}
	}

	private SubmittedCategory getSubmittedFormCategory(TreeObject category, SubmittedForm submittedForm) {
		SubmittedCategory categorySubForm = null;
		try {
			categorySubForm = (SubmittedCategory) submittedForm.getCategory(category.getName());
		} catch (CategoryDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return categorySubForm;
	}

	private SubmmitedGroup getSubmittedFormGroup(TreeObject group, IGroup iGroupSubForm) {
		SubmmitedGroup groupSubForm = null;
		try {
			if (iGroupSubForm != null) {
				groupSubForm = (SubmmitedGroup) iGroupSubForm.getGroup(group.getName());
			}
		} catch (GroupDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return groupSubForm;
	}

	private IGroup getSubmittedFormRepeatableGroup(TreeObject group, IGroup igroupSubForm, int repeatedGroupIndex) {
		List<IGroup> groups = getSubmittedFormRepeatableGroups(group, igroupSubForm);
		if (groups != null && !groups.isEmpty()) {
			return getSubmittedFormRepeatableGroups(group, igroupSubForm).get(repeatedGroupIndex);
		} else {
			return null;
		}
	}

	private List<IGroup> getSubmittedFormRepeatableGroups(TreeObject group, IGroup igroupSubForm) {
		List<IGroup> groupSubForm = null;
		if (igroupSubForm != null) {
			groupSubForm = igroupSubForm.getRepeatableGroups(group.getName());
		}
		return groupSubForm;
	}

	private SubmittedQuestion getSubmittedFormIGroupQuestion(TreeObject question, IGroup igroupSubForm) {
		SubmittedQuestion questionSubForm = null;
		try {
			if (igroupSubForm != null) {
				questionSubForm = (SubmittedQuestion) igroupSubForm.getQuestion(question.getName());
			}
		} catch (QuestionDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return questionSubForm;
	}

	private void adaptFormToRepeatableGroups(ISubmittedForm submittedForm) {
		// Get categories
		for (TreeObject category : this.form.getChildren()) {
			// Get groups (or questions)
			for (TreeObject categoryChild : category.getChildren()) {
				SubmittedCategory categorySubForm = getSubmittedFormCategory(category, (SubmittedForm) submittedForm);
				adaptFormToRepeatableGroups(categoryChild, categorySubForm);
			}
		}
	}

	private void adaptFormToRepeatableGroups(TreeObject treeObject, IGroup parentIGroupSubmitted) {
		if ((treeObject instanceof com.biit.abcd.persistence.entity.Group)
				&& ((com.biit.abcd.persistence.entity.Group) treeObject).isRepeatable()) {
			List<IGroup> repeatedGroups = getSubmittedFormRepeatableGroups(treeObject, parentIGroupSubmitted);
			// Duplicating elements this way to avoid concurrent modification
			// exception
			if (repeatedGroups != null && !repeatedGroups.isEmpty()) {
				elementsToDuplicate.add(new ElementsToDuplicate(repeatedGroups.size() - 1, treeObject));
			}
		}
		for (TreeObject child : treeObject.getChildren()) {
			if (treeObject instanceof com.biit.abcd.persistence.entity.Group) {
				adaptFormToRepeatableGroups(child, getSubmittedFormGroup(child, parentIGroupSubmitted));
			}
		}
	}

	private void duplicateRepeatedGroups() {

		System.out.println("ELEMENTS TO DUPLICATE: " + elementsToDuplicate.size());

		Collections.reverse(elementsToDuplicate);
		for (ElementsToDuplicate element : elementsToDuplicate) {
			for (int i = 0; i < element.getNumberOfCopies(); i++) {
				duplicateGroup(element.getElementToCopy());
			}
		}
	}

	private void duplicateGroup(TreeObject treeObject) {
		try {
			TreeObject parent = treeObject.getParent();
			TreeObject newGroup = treeObject.generateCopy(false, true);
			newGroup.resetIds();
			parent.addChild(newGroup);
		} catch (CharacterNotAllowedException | NotValidStorableObjectException | NotValidChildException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	class ElementsToDuplicate {
		private int numberOfCopies;
		private TreeObject elementToCopy;

		public ElementsToDuplicate(int numberOfCopies, TreeObject elementToCopy) {
			super();
			this.numberOfCopies = numberOfCopies;
			this.elementToCopy = elementToCopy;
		}

		public int getNumberOfCopies() {
			return numberOfCopies;
		}

		public void setNumberOfCopies(int numberOfCopies) {
			this.numberOfCopies = numberOfCopies;
		}

		public TreeObject getElementToCopy() {
			return elementToCopy;
		}

		public void setElementToCopy(TreeObject elementToCopy) {
			this.elementToCopy = elementToCopy;
		}
	}
}
