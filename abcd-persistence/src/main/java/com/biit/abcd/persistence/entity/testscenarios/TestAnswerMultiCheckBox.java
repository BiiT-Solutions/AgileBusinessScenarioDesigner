package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;

/**
 * Defines radio button values.
 * 
 */
@Entity
@Table(name = "test_answer_multi_checkbox")
public class TestAnswerMultiCheckBox extends TestAnswer {

	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> multiCheckBoxValue;

	public TestAnswerMultiCheckBox() {
		super();
		multiCheckBoxValue = new HashSet<String>();
	}

	@Override
	public Set<String> getValue() {
		return multiCheckBoxValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) throws NotValidAnswerValue {
		if (value != null) {
			if (!(value instanceof Set<?>)) {
				throw new NotValidAnswerValue("Expected Set<String> object in '" + value + "'");
			}
			setValue((Set<String>) value);
		}else{
			multiCheckBoxValue = null;
		}
	}

	public void setValue(Set<String> value) {
		this.multiCheckBoxValue = value;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}
}
