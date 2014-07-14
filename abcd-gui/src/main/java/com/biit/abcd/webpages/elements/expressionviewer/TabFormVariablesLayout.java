package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueDateFormCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueDateTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueFormCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.TreeObjectTableMultiSelect;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.UI;

public class TabFormVariablesLayout extends TabLayout {
	private static final long serialVersionUID = 3488733953726761594L;
	private TreeObjectTableMultiSelect formQuestionTable;
	private ListSelect variableSelection;
	private Button addTreeObjectButton, addVariableButton;

	public TabFormVariablesLayout() {
		createFormVariablesElements();
	}

	/**
	 * We can select more than one element, then we add expressions separated by commas. If we select a date question or
	 * variable, then we also must select the unit for the date expression.
	 */
	private void createFormVariablesElements() {
		initializeFormQuestionTable();
		this.setSpacing(true);
		formQuestionTable.setPageLength(10);
		addComponent(formQuestionTable);
		setExpandRatio(formQuestionTable, 0.5f);
		addTreeObjectButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_ELEMENT));
		addTreeObjectButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4754466212065015629L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!getSelectedFormElements().isEmpty()) {
					// We need to create an expression list separated by commas.
					for (int i = 0; i < getSelectedFormElements().size(); i++) {
						// Add element.
						final ExpressionValueTreeObjectReference formReference;
						// Detect if it is a date question to add units
						if ((getSelectedFormElements().get(i) instanceof Question)
								&& (((Question) getSelectedFormElements().get(i)).getAnswerFormat()) != null
								&& ((Question) getSelectedFormElements().get(i)).getAnswerFormat().equals(
										AnswerFormat.DATE)) {
							formReference = new ExpressionValueDateTreeObjectReference();
							// Create a window for selecting the unit and assign it to the expression.
							WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
									.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));

							windowDate.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									((ExpressionValueDateTreeObjectReference) formReference)
											.setUnit(((WindowSelectDateUnit) window).getValue());
									// Fire listeners to force thre refresh of GUI.
									updateExpression(formReference);
									window.close();
								}
							});
							UI.getCurrent().addWindow(windowDate);
						} else {
							// Standard element, create a normal expression.
							formReference = new ExpressionValueTreeObjectReference();
						}
						formReference.setReference(getSelectedFormElements().get(i));
						addExpression(formReference);
						// Add comma if more than one element.
						if (i < getSelectedFormElements().size() - 1) {
							ExpressionSymbol exprValue = new ExpressionSymbol();
							exprValue.setValue(AvailableSymbol.COMMA);
							addExpression(exprValue);
						}
					}
				}
			}
		});
		addComponent(addTreeObjectButton);
		setComponentAlignment(addTreeObjectButton, Alignment.TOP_RIGHT);
		initializeVariableSelection();
		addComponent(variableSelection);
		setExpandRatio(variableSelection, 0.5f);
		addVariableButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_VARIABLE));
		addVariableButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 305156770292048868L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (variableSelection.getValue() != null && !getSelectedFormElements().isEmpty()) {
					// Multiple elements must be separated by commas.
					for (int i = 0; i < getSelectedFormElements().size(); i++) {
						// Add element.
						final ExpressionValueFormCustomVariable formVariableReference;
						// Detect if it is a date question to add units
						if (((CustomVariable) variableSelection.getValue()).getType() != null
								&& ((CustomVariable) variableSelection.getValue()).getType().equals(
										CustomVariableType.DATE)) {
							formVariableReference = new ExpressionValueDateFormCustomVariable(getSelectedFormElements()
									.get(i), (CustomVariable) variableSelection.getValue());
							// Create a window for selecting the unit and assign it to the expression.
							WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
									.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));

							windowDate.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									((ExpressionValueDateFormCustomVariable) formVariableReference)
											.setUnit(((WindowSelectDateUnit) window).getValue());
									// Fire listeners to force thre refresh of GUI.
									updateExpression(formVariableReference);
									window.close();
								}
							});
							UI.getCurrent().addWindow(windowDate);
						} else {
							formVariableReference = new ExpressionValueFormCustomVariable(getSelectedFormElements()
									.get(i), (CustomVariable) variableSelection.getValue());
						}
						addExpression(formVariableReference);
						// Add comma if needed.
						if (i < getSelectedFormElements().size() - 1) {
							ExpressionSymbol exprValue = new ExpressionSymbol();
							exprValue.setValue(AvailableSymbol.COMMA);
							addExpression(exprValue);
						}
					}
				}
			}

		});
		addComponent(addVariableButton);
		setComponentAlignment(addVariableButton, Alignment.TOP_RIGHT);
		setFormVariableSelectionValues();

	}

	private void initializeFormQuestionTable() {
		formQuestionTable = new TreeObjectTableMultiSelect();
		formQuestionTable.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_ELEMENTS));
		formQuestionTable.setSizeFull();
		formQuestionTable.setRootElement((Form) UserSessionHandler.getFormController().getForm());
		formQuestionTable.setSelectable(true);
		formQuestionTable.setNullSelectionAllowed(false);
		formQuestionTable.setImmediate(true);
		formQuestionTable.setValue(UserSessionHandler.getFormController().getForm());
		formQuestionTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setFormVariableSelectionValues();
			}
		});
		formQuestionTable.collapseFrom(Category.class);
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection
				.setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_VARIABLES));
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(false);
		variableSelection.setImmediate(true);
	}

	private void setFormVariableSelectionValues() {
		if (variableSelection != null) {
			variableSelection.setValue(null);
			variableSelection.removeAllItems();
			if (!getSelectedFormElements().isEmpty()) {
				List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
						.getCustomVariables(getSelectedFormElements().get(0));
				for (CustomVariable customvariable : customVariables) {
					variableSelection.addItem(customvariable);
					variableSelection.setItemCaption(customvariable, customvariable.getName());
				}
				if (customVariables != null && !customVariables.isEmpty()) {
					variableSelection.setValue(customVariables.get(0));
				}
			}
		}
	}

	public List<ExpressionValueFormCustomVariable> getValues() {
		if (getSelectedFormElements().isEmpty() || variableSelection.getValue() == null) {
			return null;
		}

		List<ExpressionValueFormCustomVariable> variables = new ArrayList<>();
		for (TreeObject object : getSelectedFormElements()) {
			variables.add(new ExpressionValueFormCustomVariable(object, (CustomVariable) variableSelection.getValue()));
		}

		return variables;
	}

	/**
	 * Returns the selected list of element. All elements must be of the same class.
	 * 
	 * @return
	 */
	private List<TreeObject> getSelectedFormElements() {
		if ((((Collection<?>) formQuestionTable.getValue()) == null || ((Collection<?>) formQuestionTable.getValue())
				.isEmpty())) {
			return new ArrayList<>();
		}
		List<TreeObject> selected = new ArrayList<>();
		Class<?> firstClass = null;
		for (Object object : ((Collection<?>) formQuestionTable.getValue())) {
			// First object select the class of the accepted elements.
			if (firstClass == null) {
				firstClass = object.getClass();
			}
			// Only add elements that have the same class.
			if (object.getClass() == firstClass) {
				selected.add((TreeObject) object);
			}
		}
		return selected;
	}

	public void setvalue(ExpressionValueFormCustomVariable expression) {
		formQuestionTable.setValue(expression.getQuestion());
		variableSelection.setValue(expression.getVariable());
	}

	public void addTreeObjectButtonClickListener(ClickListener clickListener) {
		addTreeObjectButton.addClickListener(clickListener);
	}

	public void addVariableButtonClickListener(ClickListener clickListener) {
		addVariableButton.addClickListener(clickListener);
	}

}
