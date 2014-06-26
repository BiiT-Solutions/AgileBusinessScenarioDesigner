package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.webpages.components.PropertiesComponent;
import com.vaadin.ui.Button.ClickListener;

public class ExpressionEditorPropertiesComponent extends PropertiesComponent {
	private static final long serialVersionUID = 6024872296370510577L;

	public ExpressionEditorPropertiesComponent() {
		super();
		registerPropertiesComponent(new FormExpressionProperties());
	}

	/**
	 * FormExpressionProperties are recreated for each selected expression. Listeners must be added for the current
	 * created object.
	 * 
	 * @param clickListener
	 */
	public void addDeleteExpressionButtonClickListener(ClickListener clickListener) {
		FormExpressionProperties formExpressionProperties = ((FormExpressionProperties) getCurrentDisplayedProperties());
		if (formExpressionProperties != null) {
			formExpressionProperties.addDeleteExpressionButtonClickListener(clickListener);
		}
	}
}
