package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.TreeObjectTableMultiSelect;
import com.biit.abcd.webpages.components.WindowSelectDateUnit;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class ExpressionTabFormVariablesLayout extends TabFormVariablesLayout {
	private static final long serialVersionUID = 8469451392221408617L;
	private Button addTreeObjectButton, addVariableButton;

	public ExpressionTabFormVariablesLayout() {
		super();
		createFormVariablesElements();
	}

	/**
	 * We can select more than one element, then we add expressions separated by
	 * commas. If we select a date question or variable, then we also must
	 * select the unit for the date expression.
	 */
	private void createFormVariablesElements() {
		// Form elements list
		createFormElementsComponent();
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
						final ExpressionValueTreeObjectReference formReference = new ExpressionValueTreeObjectReference();
						formReference.setReference(getSelectedFormElements().get(i));
						// Detect if it is a date question to add units
						if ((getSelectedFormElements().get(i) instanceof Question)
								&& ((((Question) getSelectedFormElements().get(i)).getAnswerFormat()) != null)
								&& ((Question) getSelectedFormElements().get(i)).getAnswerFormat().equals(
										AnswerFormat.DATE)) {
							// Create a window for selecting the unit and assign
							// it to the expression.
							WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
									.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));
							windowDate.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									formReference.setUnit(((WindowSelectDateUnit) window).getValue());
									// Fire listeners to force the refresh of
									// GUI.
									addExpression(formReference);
									window.close();
								}
							});
							windowDate.showCentered();
						} else {
							addExpression(formReference);
						}
						// Add comma if more than one element.
						if (i < (getSelectedFormElements().size() - 1)) {
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

		// Custom variables list
		createCustomVariablesComponent();
		addVariableButton = new Button(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_BUTTON_ADD_VARIABLE));
		addVariableButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 305156770292048868L;

			@Override
			public void buttonClick(ClickEvent event) {
				if ((getVariableSelection().getValue() != null) && !getSelectedFormElements().isEmpty()) {
					// Multiple elements must be separated by commas.
					for (int i = 0; i < getSelectedFormElements().size(); i++) {
						// Add element.
						final ExpressionValueCustomVariable formVariableReference;
						// Detect if it is a date question to add units
						if ((((CustomVariable) getVariableSelection().getValue()).getType() != null)
								&& ((CustomVariable) getVariableSelection().getValue()).getType().equals(
										CustomVariableType.DATE)) {
							formVariableReference = new ExpressionValueCustomVariable(getSelectedFormElements().get(i),
									(CustomVariable) getVariableSelection().getValue());
							// Create a window for selecting the unit and assign
							// it to the expression.
							WindowSelectDateUnit windowDate = new WindowSelectDateUnit(ServerTranslate
									.translate(LanguageCodes.EXPRESSION_DATE_CAPTION));

							windowDate.addAcceptActionListener(new AcceptActionListener() {
								@Override
								public void acceptAction(AcceptCancelWindow window) {
									formVariableReference.setUnit(((WindowSelectDateUnit) window).getValue());
									// Fire listeners to force thre refresh of
									// GUI.
									updateExpression(formVariableReference);
									addExpression(formVariableReference);
									window.close();
								}
							});
							UI.getCurrent().addWindow(windowDate);
						} else {
							formVariableReference = new ExpressionValueCustomVariable(getSelectedFormElements().get(i),
									(CustomVariable) getVariableSelection().getValue());
							addExpression(formVariableReference);
						}
						// Add comma if needed.
						if (i < (getSelectedFormElements().size() - 1)) {
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
	}

	@Override
	protected void initializeFormQuestionTable() {
		setFormQuestionTable(new TreeObjectTableMultiSelect());
		getFormQuestionTable().setCaption(
				ServerTranslate.translate(LanguageCodes.EXPRESSION_FORM_VARIABLE_WINDOW_ELEMENTS));
		getFormQuestionTable().setSizeFull();
		getFormQuestionTable().setRootElement(UserSessionHandler.getFormController().getForm());
		getFormQuestionTable().setSelectable(true);
		getFormQuestionTable().setNullSelectionAllowed(false);
		getFormQuestionTable().setImmediate(true);
		getFormQuestionTable().setValue(UserSessionHandler.getFormController().getForm());
		getFormQuestionTable().addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				setFormVariableSelectionValues();
			}
		});
		getFormQuestionTable().collapseFrom(Category.class);
	}
}
