package com.biit.abcd.webpages.components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.FormController;
import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.Group;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
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
		formTreeTable.setRootElement(UserSessionHandler.getFormController().getForm());
		if (submittedForm != null) {
			generateContent((SubmittedForm) submittedForm);
		}
		setContent(formTreeTable);
	}

	private void generateContent(SubmittedForm submittedForm) {

		formTreeTable.addContainerProperty(TreeObjectTableProperties.ORIGINAL_VALUE, String.class, "");
		formTreeTable.setColumnWidth(TreeObjectTableProperties.ORIGINAL_VALUE, 150);
		FormController fc = UserSessionHandler.getFormController();
		if (fc != null) {
			Form form = fc.getForm();
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
							Category categorySubForm = getSubmittedFormCategory(category, submittedForm);
							createCategoryVariables(category, categorySubForm);
							// Put category children variables
							List<TreeObject> categoryChildren = category.getChildren();
							if (categoryChildren != null) {
								for (TreeObject categoryChild : categoryChildren) {
									if (categoryChild instanceof com.biit.abcd.persistence.entity.Group) {
										// Get the subform group
										Group groupSubForm = getSubmittedFormGroup(categoryChild, categorySubForm);
										createGroupVariables(categoryChild, groupSubForm);
										List<TreeObject> groupChildren = categoryChild.getChildren();
										if (groupChildren != null) {
											for (TreeObject groupChild : groupChildren) {
												if (groupChild instanceof com.biit.abcd.persistence.entity.Group) {
													createNestedGroupVariables(groupChild,
															getSubmittedFormGroup(groupChild, groupSubForm));

												} else if (groupChild instanceof com.biit.abcd.persistence.entity.Question) {
													createQuestionVariables(groupChild,
															getSubmittedFormGroupQuestion(groupChild, groupSubForm));
												}
											}
										}
									} else if (categoryChild instanceof com.biit.abcd.persistence.entity.Question) {
										createQuestionVariables(categoryChild,
												getSubmittedFormCategoryQuestion(categoryChild, categorySubForm));
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
				if (submittedForm.getVariableValue(variable) != null) {
					formTreeTable.getItem(form).getItemProperty(variable)
							.setValue(submittedForm.getVariableValue(variable).toString());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createCategoryVariables(TreeObject category, Category categorySubForm) {
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

	@SuppressWarnings("unchecked")
	private void createGroupVariables(TreeObject group, Group groupSubForm) {
		List<String> groupVariables = customVariablesScopeMap.get(CustomVariableScope.GROUP);

		if ((groupVariables != null) && (groupSubForm != null)) {
			for (String variable : groupVariables) {
				if (groupSubForm.getVariableValue(variable) != null) {
					formTreeTable.getItem(group).getItemProperty(variable)
							.setValue(groupSubForm.getVariableValue(variable).toString());
				} else {
					formTreeTable.getItem(group).getItemProperty(variable).setValue("-");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createQuestionVariables(TreeObject question, Question questionSubForm) {
		List<String> questionVariables = customVariablesScopeMap.get(CustomVariableScope.QUESTION);

		if ((questionVariables != null) && (questionSubForm != null)) {
			for (String variable : questionVariables) {
				if (questionSubForm.getVariableValue(variable) != null) {
					formTreeTable.getItem(question).getItemProperty(variable)
							.setValue(questionSubForm.getVariableValue(variable).toString());
				} else {
					formTreeTable.getItem(question).getItemProperty(variable).setValue("-");
				}

				if (question instanceof com.biit.abcd.persistence.entity.Question) {
					com.biit.abcd.persistence.entity.Question questionTreeObject = (com.biit.abcd.persistence.entity.Question) question;
					if (questionTreeObject.getAnswerType().equals(AnswerType.INPUT)
							&& questionTreeObject.getAnswerFormat().equals(AnswerFormat.DATE)) {
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							String formattedDate = dateFormat.format(questionSubForm.getAnswer());
							formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
									.setValue(formattedDate);
						} catch (IllegalArgumentException e) {
							AbcdLogger.errorMessage(this.getClass().getName(), e);
						}
					} else {
						formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
								.setValue(questionSubForm.getAnswer().toString());
					}
				} else {
					formTreeTable.getItem(question).getItemProperty(TreeObjectTableProperties.ORIGINAL_VALUE)
							.setValue(questionSubForm.getAnswer().toString());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createNestedGroupVariables(TreeObject group, Group groupSubForm) {
		List<String> groupVariables = customVariablesScopeMap.get(CustomVariableScope.GROUP);

		if ((groupVariables != null) && (groupSubForm != null)) {
			for (String variable : groupVariables) {
				if (groupSubForm.getVariableValue(variable) != null) {
					formTreeTable.getItem(group).getItemProperty(variable)
							.setValue(groupSubForm.getVariableValue(variable).toString());
				} else {
					formTreeTable.getItem(group).getItemProperty(variable).setValue("-");
				}
			}
		}

		// Manage the nested values
		List<TreeObject> groupChildren = group.getChildren();
		if (groupChildren != null) {
			for (TreeObject groupChild : groupChildren) {
				if (groupChild instanceof com.biit.abcd.persistence.entity.Group) {
					createNestedGroupVariables(groupChild, getSubmittedFormGroup(groupChild, groupSubForm));

				} else if (groupChild instanceof com.biit.abcd.persistence.entity.Question) {
					createQuestionVariables(groupChild, getSubmittedFormGroupQuestion(groupChild, groupSubForm));
				}
			}
		}

	}

	private Category getSubmittedFormCategory(TreeObject category, SubmittedForm submittedForm) {
		Category categorySubForm = null;
		try {
			categorySubForm = (Category) submittedForm.getCategory(category.getName());
		} catch (CategoryDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return categorySubForm;
	}

	private Group getSubmittedFormGroup(TreeObject group, Category categorySubForm) {
		Group groupSubForm = null;
		try {
			if (categorySubForm != null) {
				groupSubForm = (Group) categorySubForm.getGroup(group.getName());
			}
		} catch (GroupDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return groupSubForm;
	}

	private Group getSubmittedFormGroup(TreeObject group, Group groupSubForm) {
		Group nestedGroupSubForm = null;
		try {
			if (groupSubForm != null) {
				nestedGroupSubForm = (Group) groupSubForm.getGroup(group.getName());
			}
		} catch (GroupDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return nestedGroupSubForm;
	}

	private Question getSubmittedFormCategoryQuestion(TreeObject question, Category categorySubForm) {
		Question questionSubForm = null;
		try {
			if (categorySubForm != null) {
				questionSubForm = (Question) categorySubForm.getQuestion(question.getName());
			}
		} catch (QuestionDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return questionSubForm;
	}

	private Question getSubmittedFormGroupQuestion(TreeObject question, Group groupSubForm) {
		Question questionSubForm = null;
		try {
			if (groupSubForm != null) {
				questionSubForm = (Question) groupSubForm.getQuestion(question.getName());
			}
		} catch (QuestionDoesNotExistException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		return questionSubForm;
	}
}
