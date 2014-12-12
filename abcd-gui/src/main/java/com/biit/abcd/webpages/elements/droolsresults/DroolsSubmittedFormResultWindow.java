package com.biit.abcd.webpages.elements.droolsresults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.interfaces.ISubmittedFormElement;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.orbeon.form.ISubmittedObject;

public class DroolsSubmittedFormResultWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -9123887739972604746L;
	private HashMap<CustomVariableScope, List<String>> customVariablesScopeMap;
	private DroolsTreeObjectTable submittedFormTreeTable;

	public DroolsSubmittedFormResultWindow(SubmittedForm submittedForm, Form form) {
		super();
		setCaption("Submitted form scores");
		setWidth("60%");
		setHeight("60%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		submittedFormTreeTable = new DroolsTreeObjectTable();
		submittedFormTreeTable.setSizeFull();
		submittedFormTreeTable.setSelectable(true);
		submittedFormTreeTable.setImmediate(true);
		submittedFormTreeTable.setRootElement(submittedForm);
		generateContent(submittedForm, form);
		setContent(submittedFormTreeTable);
	}

	private void generateContent(ISubmittedObject submittedForm, Form form) {
		if ((form != null) && (submittedForm != null)) {
			// Create the columns needed for the form variables
			createVariableColumns(form);
			// Set the corresponding values in the cells of the table
			setVariables(submittedForm);
		}
	}

	private void createVariableColumns(Form form) {
		List<CustomVariable> sortedCustomVariables = new ArrayList<CustomVariable>();
		sortedCustomVariables.addAll(form.getCustomVariables(Question.class));
		sortedCustomVariables.addAll(form.getCustomVariables(Group.class));
		sortedCustomVariables.addAll(form.getCustomVariables(Category.class));
		sortedCustomVariables.addAll(form.getCustomVariables(Form.class));

		if ((sortedCustomVariables != null) && (!sortedCustomVariables.isEmpty())) {
			customVariablesScopeMap = new HashMap<CustomVariableScope, List<String>>();
			for (CustomVariable customVariable : sortedCustomVariables) {
				submittedFormTreeTable.addContainerProperty(customVariable.getName(), String.class, null);
				submittedFormTreeTable.setColumnWidth(customVariable.getName(), 150);
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
	}

	@SuppressWarnings("unchecked")
	private void setVariables(ISubmittedObject submittedFormElement) {
		if (customVariablesScopeMap != null) {
			List<String> variables = customVariablesScopeMap.get(((ISubmittedFormElement) submittedFormElement)
					.getVariableScope());
			if (variables != null) {
				for (String variable : variables) {
					if (((ISubmittedFormElement) submittedFormElement).getVariableValue(variable) != null) {
						submittedFormTreeTable
								.getItem(submittedFormElement)
								.getItemProperty(variable)
								.setValue(
										((ISubmittedFormElement) submittedFormElement).getVariableValue(variable)
												.toString());
					}
				}
			}
		}
		// Fill the children
		if (submittedFormElement.getChildren() != null) {
			for (ISubmittedObject child : submittedFormElement.getChildren()) {
				setVariables(child);
			}
		}
	}
}
