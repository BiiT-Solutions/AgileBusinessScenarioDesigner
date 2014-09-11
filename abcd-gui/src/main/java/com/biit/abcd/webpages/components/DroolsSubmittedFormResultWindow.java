package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.FormController;
import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.Group;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.TreeObject;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

public class DroolsSubmittedFormResultWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -9123887739972604746L;
	private HashMap<CustomVariableScope, List<String>> customVariablesScopeMap;
	private DroolsTreeObjectTable formTreeTable;

	protected enum TreeObjectTableProperties {
		ORIGINAL_VALUE
	}

	public DroolsSubmittedFormResultWindow(ISubmittedForm submittedForm) {
		super();
		setCaption("Submitted form scores");
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		formTreeTable = new DroolsTreeObjectTable();
		formTreeTable.setSizeFull();
		formTreeTable.setSelectable(true);
		formTreeTable.setImmediate(true);
		formTreeTable.setRootElement(UserSessionHandler.getFormController().getForm());
		generateContent((SubmittedForm) submittedForm);
		setContent(formTreeTable);
	}

	private void generateContent(SubmittedForm submittedForm) {

		formTreeTable.addContainerProperty(TreeObjectTableProperties.ORIGINAL_VALUE, String.class, "");

		FormController fc = UserSessionHandler.getFormController();
		if (fc != null) {
			Form form = fc.getForm();
			if (form != null) {
				Set<CustomVariable> customVariables = form.getCustomVariables();

				if (customVariables != null) {
					customVariablesScopeMap = new HashMap<CustomVariableScope, List<String>>();
					for (CustomVariable customVariable : customVariables) {
						switch (customVariable.getType()) {
						case NUMBER:
							formTreeTable.addContainerProperty(customVariable.getName(), Double.class, 0.);
							break;
						case STRING:
							formTreeTable.addContainerProperty(customVariable.getName(), String.class, null);
							break;
						case DATE:
							formTreeTable.addContainerProperty(customVariable.getName(), Date.class, null);
							break;
						}
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
							createCategoryVariables(category, submittedForm);
							// Put group/question variables
							List<TreeObject> groupsQuests = category.getChildren();
							if (groupsQuests != null) {
								for (TreeObject groupQuest : groupsQuests) {
									createGroupQuestionVariable(category, groupQuest, submittedForm);
									// Put question variables
									if (groupQuest instanceof com.biit.abcd.persistence.entity.Group) {
										List<TreeObject> questions = groupQuest.getChildren();
										if (questions != null) {
											for (TreeObject question : questions) {
												createQuestionVariable(category, groupQuest, question, submittedForm);
											}
										}
									}
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
		List<String> formVariables = customVariablesScopeMap.get(CustomVariableScope.FORM);
		if (formVariables != null) {
			for (String variable : formVariables) {
				formTreeTable.getItem(form).getItemProperty(variable)
						.setValue(submittedForm.getVariableValue(variable));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createCategoryVariables(TreeObject category, SubmittedForm submittedForm) {
		Category categorySubForm = null;

		try {
			categorySubForm = (Category) submittedForm.getCategory(category.getName());
		} catch (CategoryDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		List<String> categoryVariables = customVariablesScopeMap.get(CustomVariableScope.CATEGORY);
		if ((categoryVariables != null) && (categorySubForm != null)) {
			for (String variable : categoryVariables) {
				formTreeTable.getItem(category).getItemProperty(variable)
						.setValue(categorySubForm.getVariableValue(variable));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createGroupQuestionVariable(TreeObject category, TreeObject groupQuest, SubmittedForm submittedForm) {
		Category categorySubForm = null;
		try {
			categorySubForm = (Category) submittedForm.getCategory(category.getName());
		} catch (CategoryDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		if (groupQuest instanceof com.biit.abcd.persistence.entity.Group) {
			Group groupSubForm = null;
			try {
				if (categorySubForm != null) {
					groupSubForm = (Group) categorySubForm.getGroup(groupQuest.getName());
				}
			} catch (GroupDoesNotExistException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			List<String> groupVariables = customVariablesScopeMap.get(CustomVariableScope.GROUP);
			if ((groupVariables != null) && (groupSubForm != null)) {
				for (String variable : groupVariables) {
					formTreeTable.getItem(groupQuest).getItemProperty(variable)
							.setValue(groupSubForm.getVariableValue(variable));
				}
			}
		} else {
			com.biit.abcd.core.drools.facts.inputform.Question questionSubForm = null;
			try {
				if (categorySubForm != null) {
					questionSubForm = (Question) categorySubForm.getQuestion(groupQuest.getName());
				}
			} catch (QuestionDoesNotExistException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			List<String> questionVariables = customVariablesScopeMap.get(CustomVariableScope.QUESTION);
			if ((questionVariables != null) && (questionSubForm != null)) {
				for (String variable : questionVariables) {
					formTreeTable.getItem(groupQuest).getItemProperty(variable)
							.setValue(questionSubForm.getVariableValue(variable));
					formTreeTable.getItem(groupQuest).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
							.setValue(questionSubForm.getAnswer().toString());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createQuestionVariable(TreeObject category, TreeObject group, TreeObject question,
			SubmittedForm submittedForm) {
		Category categorySubForm = null;
		Group groupSubForm = null;
		Question questionSubForm = null;
		try {
			categorySubForm = (Category) submittedForm.getCategory(category.getName());
			if (categorySubForm != null) {
				groupSubForm = (Group) categorySubForm.getGroup(group.getName());
				if (groupSubForm != null) {
					questionSubForm = (Question) groupSubForm.getQuestion(question.getName());
				}
			}
		} catch (CategoryDoesNotExistException | GroupDoesNotExistException | QuestionDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}

		List<String> questionVariables = customVariablesScopeMap.get(CustomVariableScope.QUESTION);
		if ((questionVariables != null) && (questionSubForm != null)) {
			for (String variable : questionVariables) {
				formTreeTable.getItem(question).getItemProperty(variable)
						.setValue(questionSubForm.getVariableValue(variable));
				formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
						.setValue(questionSubForm.getAnswer().toString());
			}
		}

	}
}
