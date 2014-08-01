package com.biit.abcd.core.drools.facts.inputform.orbeon;

import com.biit.abcd.core.drools.facts.inputform.Category;
import com.biit.abcd.core.drools.facts.inputform.Question;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.IQuestion;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;

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
	public IQuestion createQuestion(ICategory category, String tag) {
		return new Question(tag);
	}
}
