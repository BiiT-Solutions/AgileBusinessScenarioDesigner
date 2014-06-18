package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.expressions.ExprOpLogic;
import com.biit.abcd.persistence.entity.expressions.ExprOpValue;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.webpages.components.PropertiesForClassComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class ExprOpLogicProperties extends PropertiesForClassComponent<ExprOpLogic> {

	private static final long serialVersionUID = 727775860211652782L;
	private static final String buttonWidth = "180px";

	public ExprOpLogicProperties() {
		super(ExprOpLogic.class);
	}

	@Override
	protected void setElementAbstract(final ExprOpLogic element) {

		FormLayout exprJointForm = new FormLayout();
		exprJointForm.setWidth(null);

		for (final ExprOpValue acceptedValue : element.getAcceptedValues()) {
			Button acceptedValueButton = new Button(acceptedValue.getCaption(), new ClickListener() {
				private static final long serialVersionUID = 7708969983486083437L;

				@Override
				public void buttonClick(ClickEvent event) {
					try {
						element.setValue(acceptedValue);
					} catch (NotValidOperatorInExpression e) {
						MessageManager.showError(LanguageCodes.ERROR_OPERATION_NOT_ALLOWED);
					}
					firePropertyUpdateListener(element);
				}
			});
			acceptedValueButton.setWidth(buttonWidth);
			exprJointForm.addComponent(acceptedValueButton);
		}

		addTab(exprJointForm, "TODO - ExprJointLogic", true, 0);
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
