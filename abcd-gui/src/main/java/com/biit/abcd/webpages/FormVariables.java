package com.biit.abcd.webpages;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formvariables.FormVariablesUpperMenu;
import com.biit.abcd.webpages.elements.formvariables.VariableTable;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormVariables extends FormWebPageComponent {
	private static final long serialVersionUID = 8796076485600899730L;
	private FormVariablesUpperMenu upperMenu;
	private Form form;
	private VariableTable variableTable;

	public FormVariables() {
		updateButtons(true);
	}

	@Override
	protected void initContent() {
		this.upperMenu = initUpperMenu();
		setUpperMenu(upperMenu);

		variableTable = new VariableTable();
		variableTable.setSizeFull();
		variableTable.setSelectable(true);
		variableTable.setImmediate(true);

		getWorkingAreaLayout().addComponent(variableTable);
	}

	private FormVariablesUpperMenu initUpperMenu() {
		FormVariablesUpperMenu upperMenu = new FormVariablesUpperMenu();

		upperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 7788465178005102302L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		upperMenu.addNewVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2218717226699355001L;

			@Override
			public void buttonClick(ClickEvent event) {
				CustomVariable customVariable = new CustomVariable("Variable", CustomVariableType.STRING,
						CustomVariableScope.FORM);
				addNewVariable(customVariable);
			}
		});

		upperMenu.addRemoveVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3064110201946542388L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeSelectedVariable();
			}
		});

		return upperMenu;
	}

	private void addNewVariable(CustomVariable customVariable) {
		if (variableTable != null) {
			variableTable.addRow(customVariable);
		}
	}

	private void removeSelectedVariable() {
		if (variableTable != null) {
			Object selected = variableTable.getValue();
			variableTable.removeItem(selected);
		}
	}

	private void save() {
		if (form != null) {
			try {
				UserSessionHandler.getFormController().save();
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (ConstraintViolationException cve) {
				MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_CATEGORY,
						LanguageCodes.ERROR_DATABASE_DUPLICATED_CATEGORY_CAPTION);
			}
		}
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		if (form != null) {

		}
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

}
