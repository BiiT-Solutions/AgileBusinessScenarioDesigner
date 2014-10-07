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
@Table(name = "test_answer_radio_button")
public class TestAnswerRadioButton extends TestAnswer {

	private String radioButtonValue = null;

	public TestAnswerRadioButton() {
		super();
	}

	@Override
	public String getValue() {
		return radioButtonValue;
	}

	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (value != null) {
			if (!(value instanceof String)) {
				throw new NotValidAnswerValue("Expected String object in '" + value + "'");
			}
			setValue((String) value);
		}else{
			radioButtonValue = null;
		}
	}

	public void setValue(String value) {
		this.radioButtonValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}
}
