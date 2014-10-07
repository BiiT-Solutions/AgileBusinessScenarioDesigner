package com.biit.abcd.core.drools.facts.inputform.inporter;

import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.Group;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
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
		return new Category(tag);
	}

	@Override
	public ISubmittedForm createForm(String formName, String applicationName) {
		return new SubmittedForm(formName, applicationName);
	}

	@Override
	public IGroup createGroup(ICategory category, String tag) {
		return new Group(tag);
	}
	
	@Override
	public IGroup createGroup(IGroup group, String tag) {
		return new Group(tag);
	}

	@Override
	public IQuestion createQuestion(ICategory category, String tag) {
		return new Question(tag);
	}

	@Override
	public IQuestion createQuestion(IGroup group, String tag) {
		return new Question(tag);
	}
}
