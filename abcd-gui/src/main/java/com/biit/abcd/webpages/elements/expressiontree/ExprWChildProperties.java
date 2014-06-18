package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprGroup;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;

public class ExprWChildProperties extends PropertiesForClassComponent<ExprGroup> {
	private static final long serialVersionUID = 4037946808027845321L;

	public ExprWChildProperties() {
		super(ExprGroup.class);
	}

	@Override
	protected void setElementAbstract(ExprGroup element) {
		// FormLayout formLayout = getCommonFormLayout(element);
		//
		// addTab(formLayout, "TODO - ExprWChild", true, 0);
	}

	@Override
	protected void updateElement() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void firePropertyUpdateListener() {
		// TODO Auto-generated method stub
	}

}
