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

	private long originalReferenceId;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private TestAnswer testAnswer;

	public TestScenarioQuestionAnswer() {
		super();
	}

	public TestScenarioQuestionAnswer(String name) {
		super(name);
	}

	public TestScenarioQuestionAnswer(TestAnswer testAnswer) {
		super();
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
		return super.getDefaultTechnicalName();
	}

	@Override
	public void resetIds() {
		super.resetIds();
		testAnswer.resetIds();
	}
}
