package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SelectExpressionWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -6869984694892715683L;
	private VerticalLayout rootLayout;
	private SelectExpressionTable selectExpressionTable;

	public SelectExpressionWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	private Component generateComponent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);

		selectExpressionTable = new SelectExpressionTable();
		selectExpressionTable.setSizeFull();

		for (ExpressionChain expression : UserSessionHandler.getFormController().getForm().getExpressionChains()) {
			selectExpressionTable.addRow(expression);
		}

		rootLayout.addComponent(selectExpressionTable);

		return rootLayout;
	}

	public ExpressionChain getSelectedExpression() {
		return selectExpressionTable.getSelectedExpression();
	}

	public void setSelectedExpression(ExpressionChain expression) {
		selectExpressionTable.setSelectedExpression(expression);
	}
}
