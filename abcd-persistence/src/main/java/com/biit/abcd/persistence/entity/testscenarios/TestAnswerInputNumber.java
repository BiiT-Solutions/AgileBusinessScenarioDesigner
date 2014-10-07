package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;

/**
 * Defines radio button values.
 * 
 */
@Entity
@Table(name = "test_answer_input_number")
public class TestAnswerInputNumber extends TestAnswer {

	private Double inputValue = null;

	public TestAnswerInputNumber() {
		super();
	}

	public TestAnswerInputNumber(Object number) throws NotValidAnswerValue {
		super();
		setValue(number);
	}

	@Override
	public Double getValue() {
		return inputValue;
	}

	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (value != null) {
			if (!(value instanceof Double)) {
				throw new NotValidAnswerValue("Expected Double object in '" + value + "'");
			}
			setValue((Double) value);
		}else{
			inputValue = null;
		}
	}

	public void setValue(Double value) {
		this.inputValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}
}
