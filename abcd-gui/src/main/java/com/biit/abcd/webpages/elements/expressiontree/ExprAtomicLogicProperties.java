package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.MessageManager;
import com.biit.abcd.persistence.entity.expressions.ExprAtomicLogic;
import com.biit.abcd.persistence.entity.expressions.ExprValueBoolean;
import com.biit.abcd.persistence.entity.expressions.ExprValueFormReference;
import com.biit.abcd.persistence.entity.expressions.ExprValueString;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.StringInputWindow;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class ExprAtomicLogicProperties extends ExprAtomicProperties<ExprAtomicLogic> {
	private static final long serialVersionUID = -5953203428567057845L;

	public ExprAtomicLogicProperties() {
		super(ExprAtomicLogic.class);
	}

	@Override
	protected void setElementAbstract(final ExprAtomicLogic element) {
		setStandardCommonAtomicPart(element);
		
		Button always = new Button("Always", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				element.setValue(new ExprValueBoolean(true));
				firePropertyUpdateListener(element);
			}
		});
		always.setWidth(buttonWidth);
		
		Button formElement = new Button("Select Form Element", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				SelectFormElementVariableWindow variableWindow = new SelectFormElementVariableWindow();
				variableWindow.showCentered();
				variableWindow.addAcceptAcctionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						ExprValueFormReference formReference = ((SelectFormElementVariableWindow) window).getValue();
						if (formReference == null) {
							MessageManager.showError("TODO - error");
						} else {
							window.close();
							element.setValue(formReference);
							firePropertyUpdateListener(element);
						}
					}
				});

			}
		});
		formElement.setWidth(buttonWidth);
		
		Button variableSelect = new Button("Select variable", new ClickListener() {
			private static final long serialVersionUID = -3339234972234970277L;

			@Override
			public void buttonClick(ClickEvent event) {
				SelectFormElementVariableWindow variableWindow = new SelectFormElementVariableWindow();
				variableWindow.showCentered();
				variableWindow.addAcceptAcctionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						ExprValueFormReference formReference = ((SelectFormElementVariableWindow) window).getValue();
						if (formReference == null) {
							MessageManager.showError("TODO - error");
						} else {
							window.close();
							element.setValue(formReference);
							firePropertyUpdateListener(element);
						}
					}
				});

			}
		});
		variableSelect.setWidth(buttonWidth);
		Button valueSelect = new Button("Select value", new ClickListener() {
			private static final long serialVersionUID = -8611397253545833133L;

			@Override
			public void buttonClick(ClickEvent event) {
				StringInputWindow stringInputWindow = new StringInputWindow("");
				stringInputWindow.setCaption("TODO - insert value");
				stringInputWindow.addAcceptAcctionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						String value = ((StringInputWindow) window).getValue();
						if (value == null || value.isEmpty()) {
							MessageManager.showError("TODO - error");
						} else {
							window.close();
							ExprValueString exprValue = new ExprValueString(value);
							element.setValue(exprValue);
							firePropertyUpdateListener(element);
						}
					}
				});
				stringInputWindow.showCentered();
			}
		});
		valueSelect.setWidth(buttonWidth);

		FormLayout exprAtomicMathForm = new FormLayout();
		exprAtomicMathForm.setWidth(null);
		exprAtomicMathForm.addComponent(always);
		exprAtomicMathForm.addComponent(formElement);
		exprAtomicMathForm.addComponent(variableSelect);
		exprAtomicMathForm.addComponent(valueSelect);

		addTab(exprAtomicMathForm, "TODO - ExprAtomicMath", true, 1);

	}

	@Override
	protected void updateElement() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// Do Nothing. All actions are button based and personalized. Each
		// action button will perform the update actions individually.
	}
}
