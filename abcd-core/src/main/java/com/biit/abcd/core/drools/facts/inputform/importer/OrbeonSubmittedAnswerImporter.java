package com.biit.abcd.core.drools.facts.inputform.importer;

import com.biit.abcd.core.drools.facts.inputform.SubmittedCategory;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedGroup;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
import com.biit.orbeon.OrbeonImporter;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.ISubmittedObject;

/**
 * Reads data from Orbeon Form.
 */
public class OrbeonSubmittedAnswerImporter extends OrbeonImporter {

	@Override
	public ICategory createCategory(ISubmittedObject parent, String tag) {
		ICategory category = new SubmittedCategory(tag);
		category.setParent(parent);
		return category;
	}

	@Override
	public ISubmittedForm createForm(String formName, String applicationName) {
		return new SubmittedForm(formName, applicationName);
	}

	@Override
	public IGroup createGroup(ISubmittedObject parent, String tag) {
		IGroup group = new SubmittedGroup(tag);
		group.setParent(parent);
		return group;
	}

	@Override
	public IQuestion createQuestion(ISubmittedObject parent, String tag) {
		IQuestion question = new SubmittedQuestion(tag);
		question.setParent(parent);
		return question;
	}
}
