package com.biit.abcd.webpages.elements.expressionviewer;

import java.util.HashMap;
import java.util.List;

import com.biit.abcd.persistence.entity.expressions.ExprAtomicLogic;
import com.biit.abcd.persistence.entity.expressions.ExprAtomicSymbol;
import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExprOpMath;
import com.biit.abcd.persistence.entity.expressions.ExprOpValue;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ExpressionViewer extends CssLayout {
	private static final long serialVersionUID = -3032370197806581430L;
	public static String CLASSNAME = "v-expression-viewer";
	private FormExpression formExpression;
	private ExprBasic selectedExpression = null;
	private VerticalLayout rootLayout;
	private HashMap<Button, ExprBasic> expressionOfButton;

	public ExpressionViewer() {
		setImmediate(true);
		expressionOfButton = new HashMap<>();
		setStyleName(CLASSNAME);
	}

	private void updateExpression() {
		if (formExpression != null) {
			updateExpression(formExpression);
		}
	}

	public void updateExpression(FormExpression formExpression) {
		// rootLayout.removeAllComponents();
		removeAllComponents();
		expressionOfButton = new HashMap<>();

		rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(false);
		rootLayout.setImmediate(true);
		rootLayout.setSizeFull();

		this.formExpression = formExpression;

		// One line for the expressions.
		HorizontalLayout lineLayout = new HorizontalLayout();
		lineLayout.setMargin(false);
		lineLayout.setSpacing(false);
		lineLayout.setImmediate(true);
		lineLayout.setSizeUndefined();

		addExpressions(lineLayout, formExpression.getExpressions());

		rootLayout.addComponent(lineLayout);
		updateExpressionSelectionStyles();

		addComponent(rootLayout);
	}

	private void addExpressions(HorizontalLayout lineLayout, List<ExprBasic> expressions) {
		for (ExprBasic expression : expressions) {
			addExpression(lineLayout, expression);
		}
	}

	public void addExpression(HorizontalLayout lineLayout, final ExprBasic expression) {
		final Button button = new Button(expression.getExpressionTableString(), new ClickListener() {
			private static final long serialVersionUID = 4607870570076802317L;

			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedExpression(expression);
			}

		});
		button.setStyleName("expression");
		button.addStyleName("");
		expressionOfButton.put(button, expression);

		lineLayout.addComponent(button);
		lineLayout.setExpandRatio(button, 0);
		setSelectedExpression(expression);
	}

	private void setSelectedExpression(ExprBasic expression) {
		selectedExpression = expression;
		updateExpressionSelectionStyles();
	}

	private void updateExpressionSelectionStyles() {
		for (int i = 0; i < rootLayout.getComponentCount(); i++) {
			if (rootLayout.getComponent(i) instanceof HorizontalLayout) {
				HorizontalLayout lineLayout = (HorizontalLayout) rootLayout.getComponent(i);
				for (int j = 0; j < lineLayout.getComponentCount(); j++) {
					if (lineLayout.getComponent(j) instanceof Button) {
						if (expressionOfButton.get(lineLayout.getComponent(j)).equals(selectedExpression)) {
							lineLayout.getComponent(j).setStyleName("expression-selected");
						} else {
							lineLayout.getComponent(j).setStyleName("expression-unselected");
						}
					}
				}
			}
		}
	}

	public ExprBasic getSelectedExpression() {
		return selectedExpression;
	}

	public void removeSelectedExpression() {
		if (getSelectedExpression() != null) {
			int index = formExpression.getExpressions().indexOf(getSelectedExpression());
			formExpression.getExpressions().remove(getSelectedExpression());
			ExprBasic selected = null;

			// Select next expression.
			if (index >= 0) {
				if (index < formExpression.getExpressions().size()) {
					selected = formExpression.getExpressions().get(index);
				} else if (!formExpression.getExpressions().isEmpty()) {
					selected = formExpression.getExpressions().get(formExpression.getExpressions().size() - 1);
				}
			}

			updateExpression();
			if (selected != null) {
				setSelectedExpression(selected);
			}
		}
	}

	public void addElementToSelected(ExprBasic newElement) {
		int index = formExpression.getExpressions().indexOf(getSelectedExpression()) + 1;
		if (newElement instanceof ExprAtomicSymbol) {
			// Brackets are added before selected expression in some cases.
			if (((ExprAtomicSymbol) newElement).getValue().getLeftSymbol() == true
			// Brackets always at right position in '<', '>', ... symbols.
					&& !(getSelectedExpression() instanceof ExprAtomicLogic)
					// Brackets always at right position in '=' symbol.
					&& (!(getSelectedExpression() instanceof ExprOpMath) || !((ExprOpMath) getSelectedExpression())
							.getValue().equals(ExprOpValue.ASSIGNATION))) {
				index--;
			}
		}
		if (index >= 0 && index < formExpression.getExpressions().size()) {
			formExpression.getExpressions().add(index, newElement);
		} else {
			formExpression.getExpressions().add(newElement);
		}
		System.out.println(formExpression.getExpression());
		updateExpression();
		setSelectedExpression(newElement);
	}

	public FormExpression getFormExpression() {
		return formExpression;
	}
}
