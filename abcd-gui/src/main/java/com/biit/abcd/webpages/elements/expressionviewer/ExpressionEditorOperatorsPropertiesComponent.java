package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.webpages.components.PropertiesComponent;

public class ExpressionEditorOperatorsPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = 6024872296370510577L;

	public ExpressionEditorOperatorsPropertiesComponent() {
		super();
		registerPropertiesComponent(new ExpressionOperatorsProperties());
	}
}
