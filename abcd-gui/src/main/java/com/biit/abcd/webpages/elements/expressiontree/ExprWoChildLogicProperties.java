package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprAtomicLogic;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;

public class ExprWoChildLogicProperties extends ExprAtomicProperties<ExprAtomicLogic> {
	private static final long serialVersionUID = -5953203428567057845L;

	public enum FuncOp {
		ALWAYS("Always"), ANY("Any"), QUESTION_OR_VARIABLE("Question or Variable");

		private String value;

		private FuncOp(String value) {
			this.value = value;
		}

		public String getLabel() {
			return value;
		}
	}

	private Button clean;

	public ExprWoChildLogicProperties() {
		super(ExprAtomicLogic.class);
	}

	@Override
	protected void setElementAbstract(final ExprAtomicLogic element) {
		
		final LogicExpressionWindow logicExpressionWindow = new LogicExpressionWindow(element);
		logicExpressionWindow.addAcceptAcctionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				logicExpressionWindow.close();
				firePropertyUpdateListener(logicExpressionWindow.getValue());
			}
		});

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
			}
		});
		equals.setWidth(buttonWidth);
		Button notEquals = new Button("!=", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setNe();
				logicExpressionWindow.showCentered();
			}
		});
		notEquals.setWidth(buttonWidth);
		Button lessThan = new Button("<", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setLt();
				logicExpressionWindow.showCentered();
			}
		});
		lessThan.setWidth(buttonWidth);
		Button greaterThan = new Button(">", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setGt();
				logicExpressionWindow.showCentered();
			}
		});
		greaterThan.setWidth(buttonWidth);
		Button lessEqual = new Button("<=", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setLe();
				logicExpressionWindow.showCentered();
			}
		});
		lessEqual.setWidth(buttonWidth);
		Button greaterEqual = new Button(">=", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setGe();
				logicExpressionWindow.showCentered();
			}
		});
		greaterEqual.setWidth(buttonWidth);
		Button in = new Button("IN", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setIn();
				logicExpressionWindow.showCentered();
			}
		});
		in.setWidth(buttonWidth);
		Button between = new Button("BETWEEN", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				element.setBetween();
				logicExpressionWindow.showCentered();
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

	// FormLayout formLayout = getCommonFormLayout(element);
	//
	// addTab(formLayout, "TODO - ExprWoChildLogic", true);
	//
	// clean = new Button("Clean", new ClickListener() {
	// private static final long serialVersionUID = -4680574367985072846L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// element.clean();
	// firePropertyUpdateListener(element);
	// }
	// });
	//
	// if (element.getType() == null && element.getLeftOperand() == null) {
	// functionPrimera(element);
	// }
	//
	//
	// private void functionPrimera(final ExprWoChildLogic element) {
	// ComboBox leftTypeSelection = new ComboBox("Text-1");
	// leftTypeSelection.setImmediate(true);
	// for (FuncOp op : FuncOp.values()) {
	// leftTypeSelection.addItem(op);
	// leftTypeSelection.setItemCaption(op, op.getLabel());
	// }
	//
	// Button accept = new Button("Accept", new ClickListener() {
	// private static final long serialVersionUID = -4868974846447241726L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// updateExpressionFunctionPrimera();
	// firePropertyUpdateListener(element);
	// }
	// });
	//
	// FormLayout exprWoChildForm = new FormLayout();
	// exprWoChildForm.setWidth(null);
	// exprWoChildForm.addComponent(leftTypeSelection);
	// exprWoChildForm.addComponent(accept);
	// exprWoChildForm.addComponent(clean);
	//
	// addTab(exprWoChildForm, "TODO - ExprWoChildLogicOperations", true, 1);
	// }
	//
	// private boolean updateExpressionFunctionPrimera(){
	// return true;
	// }

}
