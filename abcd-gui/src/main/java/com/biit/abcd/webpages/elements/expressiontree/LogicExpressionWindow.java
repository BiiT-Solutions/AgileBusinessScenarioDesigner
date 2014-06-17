package com.biit.abcd.webpages.elements.expressiontree;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprLeftOperand;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprValue;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprValues;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprWoChildLogic;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class LogicExpressionWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -4212298247094386855L;
	private static final String FORM_FIELD_WIDTH = "250px";

	private ExprWoChildLogic element;

	private HorizontalLayout rootLayout;
	private VerticalLayout firstComponent;
	private VerticalLayout secondComponent;
	private VerticalLayout thirdComponent;

	private FormQuestionTable formQuestionTable;
	private ListSelect variableSelection;

	private TextField firstTextField;
	private TextField secondTextField;

	private ListSelect multipleValueField;

	public LogicExpressionWindow(ExprWoChildLogic element) {
		this.element = element;

		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);

		setContent(generateComponent());

	}

	public Component generateComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);

		firstComponent = new VerticalLayout();
		firstComponent.setSizeFull();
		rootLayout.setImmediate(true);
		secondComponent = new VerticalLayout();
		secondComponent.setSizeFull();
		rootLayout.setImmediate(true);
		thirdComponent = new VerticalLayout();
		thirdComponent.setSizeFull();
		rootLayout.setImmediate(true);

		rootLayout.addComponent(firstComponent);
		rootLayout.addComponent(secondComponent);
		rootLayout.addComponent(thirdComponent);

		initializeFormQuestionTable();
		firstComponent.addComponent(formQuestionTable);
		initializeVariableSelection();
		secondComponent.addComponent(variableSelection);

		// Initialize value of formQuestionTable.
		formQuestionTable.setValue(UserSessionHandler.getFormController().getForm());

		return rootLayout;
	}

	private void initializeFormQuestionTable() {
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setCaption("Form element");
		formQuestionTable.setSizeFull();
		formQuestionTable.setRootElement((Form) UserSessionHandler.getFormController().getForm());
		formQuestionTable.setSelectable(true);
		formQuestionTable.setNullSelectionAllowed(false);
		formQuestionTable.setImmediate(true);
		formQuestionTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4088237440489679127L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TreeObject treeElement = (TreeObject) event.getProperty().getValue();
				initializeVariableSelectionValues(treeElement);
				initializeSetValueComponent();
			}
		});
	}

	private void initializeVariableSelection() {
		variableSelection = new ListSelect();
		variableSelection.setCaption("Variable");
		variableSelection.setSizeFull();
		variableSelection.setNullSelectionAllowed(true);
		variableSelection.addItem("nullItem");
		variableSelection.setNullSelectionItemId("nullItem");
		variableSelection.setItemCaption("nullItem", "None");
		variableSelection.setImmediate(true);
		variableSelection.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 7929029901780181434L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				initializeSetValueComponent();
			}
		});
	}

	private void initializeVariableSelectionValues(TreeObject treeObject) {
		variableSelection.removeAllItems();
		if (treeObject != null) {
			List<CustomVariable> customVariables = UserSessionHandler.getFormController().getForm()
					.getCustomVariables(treeObject);
			for (CustomVariable customvariable : customVariables) {
				variableSelection.addItem(customvariable);
				variableSelection.setItemCaption(customvariable, customvariable.getName());
			}
			variableSelection.setValue(null);
		}
	}

	private void initializeSetValueComponent() {
		thirdComponent.removeAllComponents();
		if (variableSelection.getValue() != null) {
			// Fields for form variables
			CustomVariable customVariable = (CustomVariable) variableSelection.getValue();
			initializeSetValueComponent(customVariable.getType());
		} else {
			// Fields for form elements
			TreeObject treeObject = (TreeObject) formQuestionTable.getValue();
			if (!formQuestionTable.isElementFiltered(treeObject)) {
				// We can only use questions.

			}

		}
	}

	private void initializeSetValueComponent(CustomVariableType type) {
		switch (element.getType()) {
		case EQ:
		case NE:
		case GE:
		case LE:
		case GT:
		case LT:
			initializeSetValueComponentSingleValue(type);
			break;
		case BETWEEN:
			break;
		case IN:
			break;
		default:
			break;
		}
	}

	private void initializeSetValueComponentSingleValue(CustomVariableType type) {
		FormLayout formLayout = new FormLayout();
		formLayout.setCaption("Set value");
		firstTextField = new TextField("Value");
		firstTextField.setWidth(FORM_FIELD_WIDTH);

		formLayout.addComponent(firstTextField);
		// Todo add check del tipo.

		thirdComponent.addComponent(formLayout);
	}

	public ExprWoChildLogic getValue() {
		updateValueQuestionVariable();
		switch (element.getType()) {
		case EQ:
		case NE:
		case GE:
		case LE:
		case GT:
		case LT:
			updateValueSingleItem();
			break;
		case BETWEEN:
			break;
		case IN:
			break;
		default:
			break;
		}
		return element;
	}

	private void updateValueQuestionVariable() {
		element.setLeftOperand(new ExprLeftOperand((TreeObject) formQuestionTable.getValue(),
				(CustomVariable) variableSelection.getValue()));
	}

	private void updateValueSingleItem() {
		// TODO now is only a string
		ExprValues values = new ExprValues();
		values.addValue(new ExprValue(firstTextField.getValue()));
		element.setRightOperand(values);
	}

}
