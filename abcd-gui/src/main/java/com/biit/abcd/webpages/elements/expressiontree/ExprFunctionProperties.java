package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprFunction;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;

/**
 * Right now the ExprFunction don't have any action or property.
 * 
 */
public class ExprFunctionProperties extends PropertiesForClassComponent<ExprFunction> {

	private static final long serialVersionUID = 4774989385396540369L;

	private ExprFunction exprFunction;

	public ExprFunctionProperties() {
		super(ExprFunction.class);
	}

	@Override
	protected void setElementAbstract(ExprFunction element) {
		exprFunction = element;
	}

	@Override
	protected void updateElement() {

	}

	@Override
	protected void firePropertyUpdateListener() {
		// firePropertyUpdateListener(exprFunction);
	}

}
