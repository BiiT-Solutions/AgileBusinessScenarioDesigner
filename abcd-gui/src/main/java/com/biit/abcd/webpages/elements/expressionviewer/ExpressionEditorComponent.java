package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class ExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;
	private ExpressionViewer expressionViewer;
	private ExpressionEditorPropertiesComponent expressionEditorProperties;

	public ExpressionEditorComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		expressionViewer = new ExpressionViewer();
		expressionViewer.setSizeFull();

		expressionEditorProperties = new ExpressionEditorPropertiesComponent();
		expressionEditorProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				expressionViewer.updateExpression((FormExpression) element);
			}
		});
		expressionEditorProperties.setSizeFull();

		rootLayout.addComponent(expressionViewer);
		rootLayout.addComponent(expressionEditorProperties);
		rootLayout.setExpandRatio(expressionViewer, 0.75f);
		rootLayout.setExpandRatio(expressionEditorProperties, 0.25f);

		setCompositionRoot(rootLayout);
	}

	protected void updatePropertiesComponent(ExprBasic value) {
		expressionEditorProperties.updatePropertiesComponent(value);
		addButtonListeners();
	}

	public void refreshExpressionEditor(FormExpression selectedExpression) {
		expressionViewer.removeAllComponents();
		if (selectedExpression != null) {
			// Add table rows.
			expressionViewer.updateExpression(selectedExpression);
		}
		expressionEditorProperties.updatePropertiesComponent(selectedExpression);
		addButtonListeners();
	}

	private void addButtonListeners() {
		expressionEditorProperties.addDeleteExpressionButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -2528193426792371129L;

			@Override
			public void buttonClick(ClickEvent event) {
				expressionViewer.removeSelectedExpression();
			}
		});
	}

}
