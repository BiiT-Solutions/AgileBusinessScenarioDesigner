package com.biit.abcd.persistence.entity.expressions.interfaces;

import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;


public interface IExpressionType<T> {

	public T getValue();

	public void setValue(T function) throws NotValidOperatorInExpression;

}
