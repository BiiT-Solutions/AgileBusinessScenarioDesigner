package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.abcd.webpages.elements.expressiontree.expression.ExprBasic;
import com.biit.abcd.webpages.elements.expressiontree.expression.ThenExpression;
import com.biit.abcd.webpages.elements.expressiontree.expression.WhenExpression;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = -3944907953375692547L;

	private HorizontalLayout rootLayout;
	private VerticalLayout expressionLayout;
	private ExpressionEditorPropertiesComponent expressionEditorProperties;
	public Form form;

	public ExpressionEditorComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		expressionLayout = new VerticalLayout();
		expressionLayout.setSizeFull();
		expressionLayout.setMargin(false);
		expressionLayout.setSpacing(true);

		expressionEditorProperties = new ExpressionEditorPropertiesComponent();
		expressionEditorProperties.setSizeFull();

		rootLayout.addComponent(expressionLayout);
		rootLayout.addComponent(expressionEditorProperties);
		rootLayout.setExpandRatio(expressionLayout, 0.75f);
		rootLayout.setExpandRatio(expressionEditorProperties, 0.25f);

		setCompositionRoot(rootLayout);
	}

	public void addWhenExpression() {

		final ExpressionTreeTable whenTable = new ExpressionTreeTable();
		whenTable.setTitle("WHEN");
		whenTable.setSizeFull();
		expressionLayout.addComponent(whenTable);

		whenTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8496011691185361507L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// Remove the update listeners.
				expressionEditorProperties.removeAllPropertyUpdateListeners();
				// Change the property element.
				ExprBasic expression = (ExprBasic) whenTable.getValue();
				updatePropertiesComponent(expression);
				// Add update listener.
				expressionEditorProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
					@Override
					public void propertyUpdate(Object element) {
						whenTable.addExpression((ExprBasic) element);
					}
				});
			}
		});

		WhenExpression whenExpression = new WhenExpression();
		whenTable.addExpression(whenExpression);
	}

	public void addThenExpression() {
		final ExpressionTreeTable thenTable = new ExpressionTreeTable();
		thenTable.setTitle("THEN");
		thenTable.setSizeFull();
		expressionLayout.addComponent(thenTable);

		thenTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8496011691185361507L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// Remove the update listeners.
				expressionEditorProperties.removeAllPropertyUpdateListeners();
				// Change the property element.
				ExprBasic expression = (ExprBasic) thenTable.getValue();
				updatePropertiesComponent(expression);
				// Add update listener.
				expressionEditorProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
					@Override
					public void propertyUpdate(Object element) {
						thenTable.addExpression((ExprBasic) element);
					}
				});
			}
		});

		ThenExpression thenExpression = new ThenExpression();
		thenTable.addExpression(thenExpression);
	}

	protected void updatePropertiesComponent(ExprBasic value) {
		expressionEditorProperties.updatePropertiesComponent(value);
	}

	// private void updateRightSideByExpression(ExprBasic expression) {
	// rightLayout.removeAllComponents();
	// if (expression == null) {
	// return;
	// }
	//
	// if (expression instanceof ExprFunction) {
	// Label nameLabel = new Label("Function: " +
	// expression.getClass().getSimpleName());
	// rightLayout.addComponent(nameLabel);
	// return;
	// }
	//
	// if (expression instanceof ExprPort) {
	// final ExprPort exprPort = (ExprPort) expression;
	// Label nameLabel = new Label("Port: " + exprPort.getName());
	// Button joinNewExpression = new Button("Add expression", new
	// ClickListener() {
	// private static final long serialVersionUID = 820753565688917978L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// exprPort.joinNewExpression();
	// expressionTable.addExpression(exprPort, true, true);
	// }
	// });
	// rightLayout.addComponent(nameLabel);
	// rightLayout.addComponent(joinNewExpression);
	// return;
	// }
	//
	// if (expression instanceof ExprWChild) {
	// final ExprWChild exprWChild = (ExprWChild) expression;
	// Label nameLabel = new Label("Function: " +
	// expression.getClass().getSimpleName());
	// Button removeParenthesis = new Button("Remove Parenthesis", new
	// ClickListener() {
	// private static final long serialVersionUID = -9014000455735891421L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// System.out.println("removeParenthesis!");
	// exprWChild.removeParenthesis();
	// // Remove parenthesis is done by removing the group element,
	// // so we need to update the parent.
	// expressionTable.removeItem(exprWChild);
	// expressionTable.addExpression(exprWChild.getParent(), true, true);
	// }
	// });
	// Button joinNewExpression = new Button("Add expression", new
	// ClickListener() {
	// private static final long serialVersionUID = -6012042054818562027L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// exprWChild.joinNewExpression();
	// expressionTable.addExpression(exprWChild, true, true);
	// }
	// });
	// rightLayout.addComponent(nameLabel);
	// rightLayout.addComponent(removeParenthesis);
	// rightLayout.addComponent(joinNewExpression);
	// return;
	// }
	//
	// if (expression instanceof ExprWoChild) {
	// final ExprWoChild exprWoChild = (ExprWoChild) expression;
	// Label nameLabel = new Label("ExprWoChild: " +
	// expression.getClass().getSimpleName());
	// Button addParenthesis = new Button("Add Parenthesis", new ClickListener()
	// {
	// private static final long serialVersionUID = 5435210375713127863L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// System.out.println("addParenthesis!");
	// exprWoChild.addParenthesis();
	// // Add parenthesis is done by adding a parent element
	// // parenthesis, so we need to update the parent as it still
	// // doesn't exist on the tree table.
	// expressionTable.addExpression(exprWoChild.getParent().getParent(), true,
	// true);
	// }
	// });
	//
	// Button removeElement = new Button("Remove", new ClickListener() {
	// private static final long serialVersionUID = 8516689932501288647L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// System.out.println("removeElement!");
	// List<ExprBasic> expressionsToRemove = exprWoChild.delete();
	// expressionTable.removeExpressions(expressionsToRemove);
	// ExprBasic elementToUpdate = exprWoChild.getParent();
	// // Retrieve the most parent element that has not been
	// // deleted.
	// while (expressionsToRemove.contains(elementToUpdate) && elementToUpdate
	// != null) {
	// elementToUpdate = elementToUpdate.getParent();
	// }
	// if (elementToUpdate != null) {
	// expressionTable.addExpression(exprWoChild.getParent().getParent(), true,
	// true);
	// }
	// }
	// });
	//
	// Button setValueButton = new Button("Set value", new ClickListener() {
	// private static final long serialVersionUID = 8986863061902964958L;
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// System.out.println("Set value! " + (form));
	// final LogicValueWindow logicValueWindow = new LogicValueWindow(form);
	// logicValueWindow.showCentered();
	// logicValueWindow.addAcceptAcctionListener(new AcceptActionListener() {
	//
	// @Override
	// public void acceptAction(AcceptCancelWindow window) {
	// ((ExprLogic) exprWoChild).value = logicValueWindow.value;
	// expressionTable.addExpression(exprWoChild, true, true);
	// logicValueWindow.close();
	// }
	// });
	// }
	// });
	//
	// rightLayout.addComponent(nameLabel);
	// rightLayout.addComponent(addParenthesis);
	// rightLayout.addComponent(removeElement);
	// rightLayout.addComponent(setValueButton);
	// return;
	// }
	//
	// if (expression instanceof ExprJoint) {
	// final ExprJoint exprJoint = (ExprJoint) expression;
	// Label nameLabel = new Label("ExprJoint: " +
	// expression.getClass().getSimpleName());
	//
	// ComboBox jointComboBox = new ComboBox();
	// jointComboBox.setImmediate(true);
	// for (JointValue value : exprJoint.getAcceptedValues()) {
	// jointComboBox.addItem(value);
	// jointComboBox.setItemCaption(value, value.getCaption());
	// }
	// jointComboBox.addValueChangeListener(new ValueChangeListener() {
	// private static final long serialVersionUID = 2645094273499256660L;
	//
	// @Override
	// public void valueChange(ValueChangeEvent event) {
	// exprJoint.setValue((JointValue) event.getProperty().getValue());
	// expressionTable.addExpression(exprJoint, true, true);
	// }
	// });
	//
	// rightLayout.addComponent(nameLabel);
	// rightLayout.addComponent(jointComboBox);
	// }
	// }
}
