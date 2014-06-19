package com.biit.abcd.persistence.entity.rules;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;

@Entity
@Table(name = "RULE_ACTION")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Action extends StorableObject {

	public abstract Object getExpression();
	
	public abstract String getExpressionAsString();

	public abstract void setExpression(Object expression) throws NotValidExpression;

	public abstract boolean undefined();
}
