package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.persistence.entity.StorableObject;

/**
 * Basic class for defining a test answer. Any other test answer must inherit from this class.
 * 
 */
@Entity
@Table(name = "test_answer_basic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class TestAnswer extends StorableObject {
	private static final long serialVersionUID = 51115793138777641L;

	public abstract Object getValue();

	public abstract void setValue(Object value) throws NotValidAnswerValue;

}
