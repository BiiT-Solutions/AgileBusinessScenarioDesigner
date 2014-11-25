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

/**
 * Reads data from Orbeon Form.
 */
public class OrbeonSubmittedAnswerImporter extends OrbeonImporter {

	@Override
	public ICategory createCategory(String tag) {
		return new SubmittedCategory(tag);
	}

	@Override
	public ISubmittedForm createForm(String formName, String applicationName) {
		return new SubmittedForm(formName, applicationName);
	}

	@Override
	public IGroup createGroup(ICategory category, String tag) {
		return new SubmittedGroup(tag);
	}
	
	@Override
	public IGroup createGroup(IGroup group, String tag) {
		return new SubmittedGroup(tag);
	}

	@Override
	public IQuestion createQuestion(ICategory category, String tag) {
		return new SubmittedQuestion(tag);
	}

	@Override
	public IQuestion createQuestion(IGroup group, String tag) {
		return new SubmittedQuestion(tag);
	}
}
