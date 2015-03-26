package com.biit.abcd.core.drools.facts.inputform.importer;

import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.orbeon.OrbeonImporter;


/**
 * Reads data from Orbeon Form.
 */
public class OrbeonSubmittedAnswerImporter extends OrbeonImporter {

	@Override
	public ISubmittedCategory createCategory(ISubmittedObject parent, String tag) {
		ISubmittedCategory category = new DroolsSubmittedCategory(tag);
		category.setParent(parent);
		return category;
	}

	@Override
	public ISubmittedForm createForm(String formName, String applicationName) {
		return new DroolsSubmittedForm(formName, applicationName);
	}

	@Override
	public ISubmittedGroup createGroup(ISubmittedObject parent, String tag) {
		ISubmittedGroup group = new DroolsSubmittedGroup(tag);
		group.setParent(parent);
		return group;
	}

	@Override
	public ISubmittedQuestion createQuestion(ISubmittedObject parent, String tag) {
		ISubmittedQuestion question = new DroolsSubmittedQuestion(tag);
		question.setParent(parent);
		return question;
	}
}
