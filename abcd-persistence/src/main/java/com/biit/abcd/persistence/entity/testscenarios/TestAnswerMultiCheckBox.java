package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Defines radio button values.
 * 
 */
@Entity
@Table(name = "test_answer_multi_checkbox")
public class TestAnswerMultiCheckBox extends TestAnswer {
	private static final long serialVersionUID = -3255342701991988332L;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "test_answer_multi_checkbox_values", joinColumns=@JoinColumn(name = "test_answer_multi_checkbox", referencedColumnName = "id"))
	@Column(name="multi_check_box_value")
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
		} else {
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

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerMultiCheckBox) {
			super.copyBasicInfo(object);
			TestAnswerMultiCheckBox testAnswerMultiCheckBox = (TestAnswerMultiCheckBox) object;
			multiCheckBoxValue = testAnswerMultiCheckBox.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of TestAnswerMultiCheckBox.");
		}
	}
}
