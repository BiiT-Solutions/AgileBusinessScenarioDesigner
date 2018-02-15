package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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
@Table(name = "test_answer_input_postalcode")
public class TestAnswerInputPostalCode extends TestAnswer {
	private static final long serialVersionUID = 2654457246294141370L;

	@Column(name = "input_value")
	private String inputValue = null;

	public TestAnswerInputPostalCode() {
		super();
	}

	@Override
	public String getValue() {
		return inputValue;
	}

	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (value != null) {
			if (!(value instanceof String)) {
				throw new NotValidAnswerValue("Expected String object in '" + value + "'");
			}
			setValue((String) value);
		} else {
			inputValue = null;
		}
	}

	public void setValue(String value) {
		this.inputValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerInputPostalCode) {
			super.copyBasicInfo(object);
			TestAnswerInputPostalCode testAnswerInputPostalCode = (TestAnswerInputPostalCode) object;
			inputValue = testAnswerInputPostalCode.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TestAnswerInputPostalCode.");
		}
	}
}
