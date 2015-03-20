package com.biit.abcd.core.drools.facts.inputform.importer;

import com.biit.form.submitted.ISubmiitedGroup;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.form.submitted.implementation.SubmittedCategory;
import com.biit.form.submitted.implementation.SubmittedForm;
import com.biit.form.submitted.implementation.SubmittedGroup;
import com.biit.form.submitted.implementation.SubmittedQuestion;
import com.biit.orbeon.OrbeonImporter;

/**
 * Reads data from Orbeon Form.
 */
public class OrbeonSubmittedAnswerImporter extends OrbeonImporter {

	@Override
	public ISubmittedCategory createCategory(ISubmittedObject parent, String tag) {
		ISubmittedCategory category = new SubmittedCategory(tag);
		category.setParent(parent);
		return category;
	}

	@Override
	public ISubmittedForm createForm(String formName, String applicationName) {
		return new SubmittedForm(formName, applicationName);
	}

	@Override
	public ISubmiitedGroup createGroup(ISubmittedObject parent, String tag) {
		ISubmiitedGroup group = new SubmittedGroup(tag);
		group.setParent(parent);
		return group;
	}

	@Override
	public ISubmittedQuestion createQuestion(ISubmittedObject parent, String tag) {
		ISubmittedQuestion question = new SubmittedQuestion(tag);
		question.setParent(parent);
		return question;
	}
}
