package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprEvent;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;

/**
 * Right now the ExprFunction don't have any action or property.
 * 
 */
public class ExprFunctionProperties extends PropertiesForClassComponent<ExprEvent> {

	private static final long serialVersionUID = 4774989385396540369L;

	private ExprEvent exprFunction;

	public ExprFunctionProperties() {
		super(ExprEvent.class);
	}

	@Override
	protected void setElementAbstract(ExprEvent element) {
		exprFunction = element;
	}

	@Override
	protected void updateElement() {

	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// firePropertyUpdateListener(exprFunction);
	}

}
