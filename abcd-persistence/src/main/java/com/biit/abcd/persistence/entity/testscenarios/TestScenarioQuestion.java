package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "test_scenario_question")
public class TestScenarioQuestion extends Question {

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private TestAnswer testAnswer;

	public TestScenarioQuestion() {
		super();
	}

	public TestScenarioQuestion(Question question) throws FieldTooLongException, CharacterNotAllowedException,
			NotValidStorableObjectException {
		super();
		this.copyData(question);
		switch (question.getAnswerType()) {
		case RADIO:
			testAnswer = new TestAnswerRadioButton();
			break;
		case MULTI_CHECKBOX:
			testAnswer = new TestAnswerMultiCheckBox();
			break;
		case INPUT:
			switch (question.getAnswerFormat()) {
			case NUMBER:
				testAnswer = new TestAnswerInputNumber();
				break;
			case POSTAL_CODE:
				testAnswer = new TestAnswerInputPostalCode();
				break;
			case TEXT:
				testAnswer = new TestAnswerInputText();
				break;
			case DATE:
				testAnswer = new TestAnswerInputDate();
				break;
			}
			break;
		}
	}

	public TestScenarioQuestion(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	public TestAnswer getTestAnswer() {
		return testAnswer;
	}

	public void setTestAnswer(TestAnswer testAnswer) {
		this.testAnswer = testAnswer;
	}
}
