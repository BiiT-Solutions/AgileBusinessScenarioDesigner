package com.biit.abcd.core.drools.facts.inputform;

import java.util.HashMap;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.interfaces.ISubmittedFormElement;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.ISubmittedObject;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

/**
 * Basic implementation of an Orbeon Form that includes categories and questions.
 * 
 */
public class SubmittedForm extends com.biit.form.submitted.SubmittedForm implements ISubmittedFormElement {
	// TreeObject -> VarName --> ValuegetVariableScope
	private HashMap<Object, HashMap<String, Object>> formVariables;

	public SubmittedForm(String applicationName, String formName) {
		super(applicationName, formName);
	}

	public SubmittedForm(String formName) {
		super("", formName);
	}

	@Override
	public String getId() {
		if ((this.getApplicationName() != null) && (this.getName() != null)) {
			return this.getApplicationName() + "/" + this.getName();
		}
		return null;
	}

	@Override
	public Object getVariableValue(Class<?> type, String varName) {
		List<ISubmittedObject> childs = getChildren(type);

		if (childs != null && !childs.isEmpty()) {
			return getVariableValue(childs.get(0), varName);
		}
		return null;
	}

	@Override
	public Object getVariableValue(Class<?> type, String treeObjectName, String varName) {

		ISubmittedObject selectedObject = null;
		// Check this element.
		if (type.isInstance(this)) {
			if (this.getTag().equals(treeObjectName)) {
				return this;
			}
		}

		// Check the children.
		if (selectedObject == null) {
			selectedObject = getChild(type, treeObjectName);
		}

		if (selectedObject != null) {
			return getVariableValue(selectedObject, varName);
		}
		return null;
	}

	@Override
	public Object getVariableValue(Object submmitedFormObject, String varName) {
		if ((formVariables == null) || (formVariables.get(submmitedFormObject) == null)) {
			return null;
		}
		return formVariables.get(submmitedFormObject).get(varName);
	}

	@Override
	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	@Override
	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		return isScoreSet(this, varName);
	}

	@Override
	public boolean isScoreSet(Object submittedFormTreeObject, String varName) {
		return !((formVariables == null) || (formVariables.get(submittedFormTreeObject) == null) || (formVariables.get(
				submittedFormTreeObject).get(varName) == null));
	}

	@Override
	public void setVariableValue(Object submittedFormTreeObject, String varName, Object value) {
		if (value != null) {
			if (formVariables == null) {
				formVariables = new HashMap<Object, HashMap<String, Object>>();
			}
			if (formVariables.get(submittedFormTreeObject) == null) {
				formVariables.put(submittedFormTreeObject, new HashMap<String, Object>());
			}
			formVariables.get(submittedFormTreeObject).put(varName, value);
		}
	}

	@Override
	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	@Override
	public String toString() {
		String text = getName() + " (" + this.getClass().getSimpleName() + ")";
		for (ISubmittedObject child : getChildren()) {
			text += " " + child.toString();
		}
		return text;
	}

	public HashMap<Object, HashMap<String, Object>> getFormVariables() {
		return formVariables;
	}

	public ISubmittedForm getSubmittedForm() {
		return this;
	}

	public IQuestion getQuestion(String categoryName, String questionName) throws QuestionDoesNotExistException,
			CategoryDoesNotExistException {
		return (IQuestion) getChild(ICategory.class, categoryName).getChild(IQuestion.class, questionName);
	}

	@Override
	public String getOriginalValue() {
		return "";
	}

	@Override
	public CustomVariableScope getVariableScope() {
		return CustomVariableScope.FORM;
	}

	@Override
	public String generateXML(String tabs) {
		String xmlFile = "<" + getName() + " type=\"" + this.getClass().getSimpleName() + "\"" + ">\n";
		for (ISubmittedObject child : getChildren()) {
			xmlFile += ((ISubmittedFormElement) child).generateXML("\t");
		}
		xmlFile += "</" + getName() + ">";
		return xmlFile;
	}

	public String generateXML() {
		return generateXML("");
	}

}
