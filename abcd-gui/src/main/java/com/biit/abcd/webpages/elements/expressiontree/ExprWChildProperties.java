package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.webpages.elements.expressiontree.expression.ExprWChild;
import com.vaadin.ui.FormLayout;

public class ExprWChildProperties extends ExprWoChildProperties<ExprWChild> {
	private static final long serialVersionUID = 4037946808027845321L;

	public ExprWChildProperties() {
		super(ExprWChild.class);
	}

	@Override
	protected void setElementAbstract(ExprWChild element) {
		FormLayout formLayout = getCommonFormLayout(element);

		addTab(formLayout, "TODO - ExprWChild", true, 0);
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
