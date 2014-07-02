package com.biit.abcd.webpages.elements.expressionviewer;

import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class ExpressionElement extends CssLayout {
	private static final long serialVersionUID = -1658928610594293678L;
	private static final String HEIGHT = "25px";
	private static final String STYLE = "v-expression-element";
	private Label elementName;

	public ExpressionElement(String label, LayoutClickListener clickListener) {
		elementName = new Label(label);

		this.setWidth(null);
		this.setHeight(HEIGHT);
		this.setStyleName(STYLE);

		addComponent(elementName);
		setImmediate(true);

		this.addLayoutClickListener(clickListener);
	}

}
