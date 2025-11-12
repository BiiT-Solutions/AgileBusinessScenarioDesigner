package com.biit.abcd.core.testscenarios;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.testscenarios.validator.TestScenarioValidator;
import com.biit.abcd.core.testscenarios.validator.TestScenarioValidatorMessage;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputDate;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerMultiCheckBox;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioGroup;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.ISubmittedQuestion;

/**
 * This class transforms a Form in a TestScenario.<br>
 * Test scenarios can be executed by the drools engine and a result can be
 * presented to the user.
 */
public class TestScenarioDroolsSubmittedForm {
	private final static String DEFAULT_APPLICATION_NAME = "ABCD";

	private TestScenarioValidator testValidator = null;
	private ISubmittedForm submittedForm;

	public ISubmittedForm createSubmittedForm(Form form, BaseForm testScenario) {
		if ((form != null) && (testScenario != null)) {
			// If the test scenario is a subset of the form passed, we parse the
			// structure
			testValidator = new TestScenarioValidator();
			testValidator.checkAndModifyTestScenarioStructure(form, testScenario);
			createDroolsSubmittedForm(testScenario);
		}
		createSubmittedFormFile(getSubmitedForm());
		return getSubmitedForm();
	}

	private void createSubmittedFormFile(ISubmittedForm submittedForm) {
		if (submittedForm != null) {
			try {
				String submmitedFormXml = ((DroolsSubmittedForm) submittedForm).generateXML();
				Files.write(
						Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedSubmittedForm.xml"),
						submmitedFormXml.getBytes("UTF-8"));
			} catch (IOException e) {
				AbcdLogger.errorMessage(TestScenarioDroolsSubmittedForm.class.getName(), e);
			}
		}
	}

	public List<TestScenarioValidatorMessage> getTestScenarioModifications() {
		return testValidator.getValidatorMessages();
	}

	private void createDroolsSubmittedForm(BaseForm form) {
		setSubmittedForm(new DroolsSubmittedForm(DEFAULT_APPLICATION_NAME, form.getLabel(), form.getVersion()));
		createSubmittedFromStructure(form, getSubmitedForm());
	}

	private void createSubmittedFromStructure(TreeObject treeObject, ISubmittedObject parent) {
		for (TreeObject child : treeObject.getChildren()) {
			if (child instanceof TestScenarioCategory) {
				ISubmittedObject droolsCategory = createCategory(parent, child.getName());
				createSubmittedFromStructure(child, droolsCategory);
			} else if (child instanceof TestScenarioGroup) {
				ISubmittedObject droolsGroup = createGroup(parent, child.getName());
				createSubmittedFromStructure(child, droolsGroup);
			} else if (child instanceof TestScenarioQuestion) {
				TestAnswer testAnswer = ((TestScenarioQuestion) child).getTestAnswer();
				if ((testAnswer != null) && (testAnswer.getValue() != null)) {
					if (testAnswer instanceof TestAnswerMultiCheckBox) {
						Set<String> values = ((TestAnswerMultiCheckBox) testAnswer).getValue();
						if ((values != null) && !values.isEmpty()) {
							ISubmittedQuestion question = (ISubmittedQuestion) createQuestion(parent, child.getName());
							for (String value : values) {
								question.addAnswer(value);
							}
						}
					} else {
						ISubmittedQuestion question = (ISubmittedQuestion) createQuestion(parent, child.getName());
						if (testAnswer instanceof TestAnswerInputDate) {
							Timestamp timeStamp = ((TestAnswerInputDate) testAnswer).getValue();
							Date date = new Date(timeStamp.getTime());
							question.addAnswer(date.toString());
						} else {
							question.addAnswer(testAnswer.getValue().toString());
						}
					}
				} else {
					// Create empty question
					createQuestion(parent, child.getName());
				}
			}
		}
	}

	private ISubmittedObject createCategory(ISubmittedObject parent, String tag) {
		ISubmittedCategory category = new DroolsSubmittedCategory(tag);
		parent.addChild(category);
		return category;
	}

	private ISubmittedObject createGroup(ISubmittedObject parent, String tag) {
		ISubmittedGroup group = new DroolsSubmittedGroup(tag);
		parent.addChild(group);
		return group;
	}

	private ISubmittedObject createQuestion(ISubmittedObject parent, String tag) {
		ISubmittedQuestion question = new DroolsSubmittedQuestion(tag);
		parent.addChild(question);
		return question;
	}

	public ISubmittedForm getSubmitedForm() {
		return submittedForm;
	}

	private void setSubmittedForm(ISubmittedForm submittedForm) {
		this.submittedForm = submittedForm;
	}
}
