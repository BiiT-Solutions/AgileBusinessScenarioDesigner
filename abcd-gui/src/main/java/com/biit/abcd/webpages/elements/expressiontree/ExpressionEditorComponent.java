package com.biit.abcd.webpages.elements.expressiontree;

import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.ExpressionWhen;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
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

	public ExpressionTreeTable addWhenExpression(String caption) {
		final ExpressionTreeTable whenTable = new ExpressionTreeTable();
		whenTable.setTitle(caption);
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

		ExpressionWhen whenExpression = new ExpressionWhen();
		whenExpression.addDefaultChild();
		whenTable.addExpression(whenExpression);

		return whenTable;
	}

	public ExpressionTreeTable addThenExpression(String caption) {
		final ExpressionTreeTable thenTable = new ExpressionTreeTable();
		thenTable.setTitle(caption);
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

		return thenTable;
	}

	protected void updatePropertiesComponent(ExprBasic value) {
		expressionEditorProperties.updatePropertiesComponent(value);
	}
}
