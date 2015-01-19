package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines radio button values.
 * 
 */
@Entity
@Table(name = "test_answer_input_number")
public class TestAnswerInputNumber extends TestAnswer {
	private static final long serialVersionUID = -7516410984463406831L;
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
		} else {
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

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerInputNumber) {
			super.copyBasicInfo(object);
			TestAnswerInputNumber testAnswerInputNumber = (TestAnswerInputNumber) object;
			inputValue = testAnswerInputNumber.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of TestAnswerInputNumber.");
		}
	}
}
