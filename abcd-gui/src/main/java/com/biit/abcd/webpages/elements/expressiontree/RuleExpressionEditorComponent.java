package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExpressionThen;
import com.biit.abcd.persistence.entity.expressions.ExpressionWhen;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class RuleExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = -3032639915566119408L;

	private HorizontalLayout rootLayout;
	private VerticalLayout expressionLayout;
	private ExpressionEditorPropertiesComponent expressionEditorProperties;
	private ExpressionTreeTable whenTable;
	private ExpressionTreeTable thenTable;
	private ValueChangeListener whenTableValueChangeListener;
	private ValueChangeListener thenTableValueChangeListener;

	public RuleExpressionEditorComponent() {
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

		initWhenTable();
		initThenTable();

		rootLayout.addComponent(expressionLayout);
		rootLayout.addComponent(expressionEditorProperties);
		rootLayout.setExpandRatio(expressionLayout, 0.75f);
		rootLayout.setExpandRatio(expressionEditorProperties, 0.25f);

		setCompositionRoot(rootLayout);
	}

	private void initThenTable() {
		thenTable = new ExpressionTreeTable();
		thenTable.setTitle("THEN");
		thenTable.setSizeFull();

		thenTableValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -8496011691185361507L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				whenTable.removeValueChangeListener(whenTableValueChangeListener);
				whenTable.setValue(null);
				whenTable.addValueChangeListener(whenTableValueChangeListener);
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
						thenTable.setValue((ExprBasic) element);
					}
				});
			}
		};
		thenTable.addValueChangeListener(thenTableValueChangeListener);
		expressionLayout.addComponent(thenTable);

		ExpressionThen thenExpression = new ExpressionThen();
		thenExpression.addDefaultChild();
		thenTable.addExpression(thenExpression);
	}

	private void initWhenTable() {
		whenTable = new ExpressionTreeTable();
		whenTable.setTitle("WHEN");
		whenTable.setSizeFull();

		whenTableValueChangeListener = new ValueChangeListener() {
			private static final long serialVersionUID = -8496011691185361507L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				thenTable.removeValueChangeListener(thenTableValueChangeListener);
				thenTable.setValue(null);
				thenTable.addValueChangeListener(thenTableValueChangeListener);
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
						whenTable.setValue((ExprBasic) element);
					}
				});
			}
		};
		whenTable.addValueChangeListener(whenTableValueChangeListener);
		expressionLayout.addComponent(whenTable);

		ExpressionWhen whenExpression = new ExpressionWhen();
		whenExpression.addDefaultChild();
		whenTable.addExpression(whenExpression);
	}

	protected void updatePropertiesComponent(ExprBasic value) {
		expressionEditorProperties.updatePropertiesComponent(value);
	}
}
