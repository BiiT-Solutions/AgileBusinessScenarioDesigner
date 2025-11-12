package com.biit.abcd.persistence.entity.testscenarios;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
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

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "test_scenario_question")
@Cacheable(true)
public class TestScenarioQuestion extends BaseQuestion {
	private static final long serialVersionUID = -3285383319148456954L;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "test_answer")
	private TestAnswer testAnswer;

	private static final String DEFAULT_QUESTION_NAME = "TestScenarioQuestion";

	public TestScenarioQuestion() {
		super();
	}

	public TestAnswer getTestAnswer() {
		return testAnswer;
	}

	public void setTestAnswer(TestAnswer testAnswer) {
		this.testAnswer = testAnswer;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// not implemented
	}

	@Override
	public String getDefaultTechnicalName() {
		return DEFAULT_QUESTION_NAME;
	}

	public TestScenarioQuestion copyTestScenarioQuestion() throws FieldTooLongException, CharacterNotAllowedException {
		TestScenarioQuestion testScenarioQuestion = new TestScenarioQuestion();
		testScenarioQuestion.setOriginalReference(getOriginalReference());
		testScenarioQuestion.setName(getName());
		return testScenarioQuestion;
	}
}
