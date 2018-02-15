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
@Table(name = "test_answer_radio_button")
public class TestAnswerRadioButton extends TestAnswer {
	private static final long serialVersionUID = 6414765646541131563L;
	
	@Column(name = "radio_button_value")
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
		} else {
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

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerRadioButton) {
			super.copyBasicInfo(object);
			TestAnswerRadioButton testAnswerRadioButton = (TestAnswerRadioButton) object;
			radioButtonValue = testAnswerRadioButton.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of TestAnswerRadioButton.");
		}
	}
}
