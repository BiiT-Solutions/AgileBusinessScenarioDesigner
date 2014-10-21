package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "test_scenario_question_answer")
public class TestScenarioQuestionAnswer extends TestScenarioObject {

	private static final String DEFAULT_QUESTION_TECHNICAL_NAME = "testScenarioObject";

	private long originalReferenceId;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private TestAnswer testAnswer;

	public TestScenarioQuestionAnswer() {
	}

	public TestScenarioQuestionAnswer(TestAnswer testAnswer) {
		setTestAnswer(testAnswer);
	}

	public TestAnswer getTestAnswer() {
		return testAnswer;
	}

	public void setTestAnswer(TestAnswer testAnswer) {
		this.testAnswer = testAnswer;
	}

	public long getOriginalReferenceId() {
		return originalReferenceId;
	}

	public void setOriginalReferenceId(long originalReferenceId) {
		this.originalReferenceId = originalReferenceId;
	}

	@Override
	public void copyData(StorableObject object) {
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_QUESTION_TECHNICAL_NAME;
	}

}
