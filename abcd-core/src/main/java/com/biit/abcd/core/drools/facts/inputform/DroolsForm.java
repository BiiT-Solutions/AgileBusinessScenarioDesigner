package com.biit.abcd.core.drools.facts.inputform;

import java.util.List;

import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.form.submitted.implementation.SubmittedForm;

/**
 * Needed to allow drools to manage variables in memory.<br>
 * It is used as the parent class of the submitted form, to allow this form to manage variable.<br>
 */
public class DroolsForm implements ISubmittedForm {

	private ISubmittedForm submittedform;

	public DroolsForm(ISubmittedForm submittedForm) {
		this.submittedform = submittedForm;
	}

	public ISubmittedForm getSubmittedForm() {
		return submittedform;
	}

	public ISubmittedQuestion getQuestion(String categoryName, String questionName) {
		return (ISubmittedQuestion) submittedform.getChild(ISubmittedCategory.class, categoryName)
				.getChild(ISubmittedQuestion.class, questionName);
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
