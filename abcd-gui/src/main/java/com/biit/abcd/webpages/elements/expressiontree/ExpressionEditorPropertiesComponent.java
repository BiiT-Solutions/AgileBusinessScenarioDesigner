package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.webpages.components.PropertiesComponent;

public class ExpressionEditorPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = 7925262071764165339L;

	public ExpressionEditorPropertiesComponent() {
		super();
		registerPropertiesComponent(new ExprFunctionProperties());
		registerPropertiesComponent(new ExprAtomicLogicProperties());
		registerPropertiesComponent(new ExprAtomicMathProperties());
		registerPropertiesComponent(new ExprGroupProperties());
		registerPropertiesComponent(new ExprOpMathProperties());
		registerPropertiesComponent(new ExprOpLogicProperties());
	}
}
