package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
		// TODO Auto-generated method stub
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
