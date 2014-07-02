package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Component for editing an expression. Is composed by a viewer and a properties menu.
 */
public class ExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;
	private ExpressionViewer expressionViewer;
	private ExpressionEditorPropertiesComponent expressionEditorProperties;

	private FormExpression formExpression;

	public ExpressionEditorComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(false);
		rootLayout.setSpacing(true);

		VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setMargin(false);
		viewLayout.setSpacing(false);

		expressionViewer = new ExpressionViewer();
		expressionViewer.setSizeFull();

		expressionEditorProperties = new ExpressionEditorPropertiesComponent();
		expressionEditorProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				expressionViewer.updateExpression((FormExpression) element);
			}
		});
		expressionEditorProperties.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((Expression) newElement);
			}

		});
		expressionEditorProperties.setSizeFull();

		viewLayout.addComponent(expressionViewer);

		rootLayout.addComponent(viewLayout);
		rootLayout.addComponent(expressionEditorProperties);
		rootLayout.setExpandRatio(viewLayout, 0.75f);
		rootLayout.setExpandRatio(expressionEditorProperties, 0.25f);

		setCompositionRoot(rootLayout);
	}

	protected void updatePropertiesComponent(Expression value) {
		expressionEditorProperties.updatePropertiesComponent(value);
		addButtonListeners();
	}

	public void refreshExpressionEditor(FormExpression selectedExpression) {
		this.formExpression = selectedExpression;
		expressionViewer.removeAllComponents();
		if (selectedExpression != null) {
			// Add table rows.
			expressionViewer.updateExpression(selectedExpression);
			expressionEditorProperties.updatePropertiesComponent(selectedExpression);
			addButtonListeners();
		}
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
