package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.webpages.components.PropertiesComponent;
import com.vaadin.ui.Button.ClickListener;

public class ExpressionEditorGlobalConstantsPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = 6024872296370510577L;

	public ExpressionEditorGlobalConstantsPropertiesComponent() {
		super();
		registerPropertiesComponent(new ExpressionOperatorsProperties());
	}
}
