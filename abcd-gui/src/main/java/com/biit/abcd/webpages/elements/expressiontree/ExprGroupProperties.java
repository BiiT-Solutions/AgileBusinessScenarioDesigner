package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprGroup;
import com.vaadin.ui.FormLayout;

public class ExprGroupProperties extends ExprAtomicProperties<ExprGroup> {
	private static final long serialVersionUID = 4037946808027845321L;

	public ExprGroupProperties() {
		super(ExprGroup.class);
	}

	@Override
	protected void setElementAbstract(ExprGroup element) {
		FormLayout formLayout = getCommonFormLayout(element);

		addTab(formLayout, "TODO - ExprGroup", true, 0);
	}

	@Override
	protected void updateElement() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
	}

}
