package com.biit.abcd.core.drools.facts.inputform;

import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.ISubmittedObject;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

/**
 * Needed to allow drools to manage variables in memory.<br>
 * It is used as the parent class of the submitted form, to allow this form to manage variable.<br>
 * 
 */
public class DroolsForm implements ISubmittedForm {

	private SubmittedForm submittedform;

	public DroolsForm(SubmittedForm submittedForm) {
		this.submittedform = submittedForm;
	}

	public SubmittedForm getSubmittedForm() {
		return submittedform;
	}

	public IQuestion getQuestion(String categoryName, String questionName) throws QuestionDoesNotExistException,
			CategoryDoesNotExistException {
		return (IQuestion) submittedform.getChild(ICategory.class, categoryName)
				.getChild(IQuestion.class, questionName);
	}

	@Override
	public String getName() {
		return submittedform.getName();
	}

	public String getApplicationName() {
		return submittedform.getApplicationName();
	}

	public String getId() {
		return submittedform.getId();
	}

	public void setSubmittedForm(SubmittedForm submittedForm) {
		this.submittedform = submittedForm;
	}

	@Override
	public String getTag() {
		return getSubmittedForm().getTag();
	}

	@Override
	public void setTag(String tag) {
		getSubmittedForm().setTag(tag);
	}

	@Override
	public String getText() {
		return getSubmittedForm().getText();
	}

	@Override
	public void setText(String text) {
		getSubmittedForm().setText(text);
	}

	@Override
	public ISubmittedObject getParent() {
		return getSubmittedForm().getParent();
	}

	@Override
	public void setParent(ISubmittedObject parent) {
		getSubmittedForm().setParent(parent);
	}

	@Override
	public void addChild(ISubmittedObject child) {
		getSubmittedForm().addChild(child);
	}

	@Override
	public List<ISubmittedObject> getChildren() {
		return getSubmittedForm().getChildren();
	}

	@Override
	public void setChildren(List<ISubmittedObject> children) {
		getSubmittedForm().setChildren(children);
	}

	@Override
	public ISubmittedObject getChild(Class<?> type, String tag) {
		return getSubmittedForm().getChild(type, tag);
	}

	@Override
	public List<ISubmittedObject> getChildren(Class<?> type) {
		return getSubmittedForm().getChildren(type);
	}

	@Override
	public String toString() {
		return getSubmittedForm().toString();
	}

	@Override
	public String getPathName() {
		return submittedform.getPathName();
	}

	@Override
	public Integer getIndex(ISubmittedObject child) {
		return submittedform.getIndex(child);
	}

	@Override
	public int compareTo(ISubmittedObject arg0) {
		return submittedform.compareTo(arg0);
	}

	@Override
	public int getLevel() {
		return submittedform.getLevel();
	}
}
