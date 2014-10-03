package com.biit.abcd.persistence.entity.testscenarios;

import java.sql.Timestamp;
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
@Table(name = "test_answer_input_date")
public class TestAnswerInputDate extends TestAnswer {

	private Timestamp dateValue = null;

	public TestAnswerInputDate() {
		super();
	}

	@Override
	public Timestamp getValue() {
		return dateValue;
	}

	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (!(value instanceof Timestamp)) {
			throw new NotValidAnswerValue("Expected Timestamp object in '" + value + "'");
		}
		setValue((Timestamp) value);
	}

	public void setValue(Timestamp value) {
		this.dateValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}
}
