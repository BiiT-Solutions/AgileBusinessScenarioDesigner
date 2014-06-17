package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.webpages.elements.expressiontree.expression.ExprWoChildLogic;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class ExprWoChildLogicProperties extends ExprWoChildProperties<ExprWoChildLogic> {
	private static final long serialVersionUID = -5953203428567057845L;

	public ExprWoChildLogicProperties() {
		super(ExprWoChildLogic.class);
	}

	@Override
	protected void setElementAbstract(final ExprWoChildLogic element) {
		final LogicExpressionWindow logicExpressionWindow = new LogicExpressionWindow(element);
		
		FormLayout formLayout = getCommonFormLayout(element);

		addTab(formLayout, "TODO - ExprWoChildLogic", true);

		Button always = new Button("ALWAYS", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setAlways();
				firePropertyUpdateListener(element);
			}
		});
		always.setWidth(buttonWidth);
		Button equals = new Button("==", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setEq();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		equals.setWidth(buttonWidth);
		Button notEquals = new Button("!=", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setNe();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		notEquals.setWidth(buttonWidth);
		Button lessThan = new Button("<", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setLt();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		lessThan.setWidth(buttonWidth);
		Button greaterThan = new Button(">", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setGt();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		greaterThan.setWidth(buttonWidth);
		Button lessEqual = new Button("<=", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setLe();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		lessEqual.setWidth(buttonWidth);
		Button greaterEqual = new Button(">=", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setGe();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		greaterEqual.setWidth(buttonWidth);
		Button in = new Button("IN", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setIn();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		in.setWidth(buttonWidth);
		Button between = new Button("BETWEEN", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setBetween();
				logicExpressionWindow.showCentered();
				firePropertyUpdateListener(element);
			}
		});
		between.setWidth(buttonWidth);

		FormLayout exprWoChildForm = new FormLayout();
		exprWoChildForm.setWidth(null);
		exprWoChildForm.addComponent(always);
		exprWoChildForm.addComponent(equals);
		exprWoChildForm.addComponent(notEquals);
		exprWoChildForm.addComponent(lessThan);
		exprWoChildForm.addComponent(greaterThan);
		exprWoChildForm.addComponent(lessEqual);
		exprWoChildForm.addComponent(greaterEqual);
		exprWoChildForm.addComponent(in);
		exprWoChildForm.addComponent(between);
		
		addTab(exprWoChildForm, "TODO - ExprWoChildLogicOperations", true, 1);

	}

	@Override
	protected void updateElement() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void firePropertyUpdateListener() {
		// Do Nothing. All actions are button based and personalized. Each
		// action button will perform the update actions individually.
	}

}
