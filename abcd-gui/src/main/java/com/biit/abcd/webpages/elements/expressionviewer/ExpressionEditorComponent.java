package com.biit.abcd.webpages.elements.expressionviewer;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.ExprBasic;
import com.biit.abcd.persistence.entity.expressions.FormExpression;
import com.biit.abcd.webpages.components.ElementAddedListener;
import com.biit.abcd.webpages.components.PropertieUpdateListener;
import com.biit.jexeval.ExpressionEvaluator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ExpressionEditorComponent extends CustomComponent {
	private static final long serialVersionUID = 3094049792744722628L;
	private HorizontalLayout rootLayout;
	private ExpressionViewer expressionViewer;
	private ExpressionEditorPropertiesComponent expressionEditorProperties;
	private Label evaluatorOutput;
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

		HorizontalLayout evaluatorLayout = createEvaluatorLayout();

		expressionViewer = new ExpressionViewer();
		expressionViewer.setSizeFull();

		expressionEditorProperties = new ExpressionEditorPropertiesComponent();
		expressionEditorProperties.addPropertyUpdateListener(new PropertieUpdateListener() {
			@Override
			public void propertyUpdate(Object element) {
				expressionViewer.updateExpression((FormExpression) element);
				updateEvaluator((FormExpression) element);
			}
		});
		expressionEditorProperties.addNewElementListener(new ElementAddedListener() {

			@Override
			public void elementAdded(Object newElement) {
				expressionViewer.addElementToSelected((ExprBasic) newElement);
				updateEvaluator(formExpression);
			}

		});
		expressionEditorProperties.setSizeFull();

		viewLayout.addComponent(evaluatorLayout);
		viewLayout.setExpandRatio(evaluatorLayout, 0.08f);
		viewLayout.setComponentAlignment(evaluatorLayout, Alignment.BOTTOM_RIGHT);
		viewLayout.addComponent(expressionViewer);
		viewLayout.setExpandRatio(expressionViewer, 0.92f);

		rootLayout.addComponent(viewLayout);
		rootLayout.addComponent(expressionEditorProperties);
		rootLayout.setExpandRatio(viewLayout, 0.75f);
		rootLayout.setExpandRatio(expressionEditorProperties, 0.25f);

		setCompositionRoot(rootLayout);
	}

	private void updateEvaluator(FormExpression formExpression) {
		try {
			new ExpressionEvaluator(formExpression.getExpression()).eval();
			evaluatorOutput.setStyleName("expression-valid");
			evaluatorOutput.setValue(ServerTranslate.tr(LanguageCodes.EXPRESSION_CHECKER_VALID));
		} catch (Exception e) {
			evaluatorOutput.setStyleName("expression-invalid");
			evaluatorOutput.setValue(ServerTranslate.tr(LanguageCodes.EXPRESSION_CHECKER_INVALID));
		}
	}

	private HorizontalLayout createEvaluatorLayout() {
		HorizontalLayout checkerLayout = new HorizontalLayout();
		checkerLayout.setMargin(false);
		checkerLayout.setSpacing(false);
		checkerLayout.setSizeFull();

		evaluatorOutput = new Label("Check");
		evaluatorOutput.setSizeUndefined();
		checkerLayout.addComponent(evaluatorOutput);
		checkerLayout.setComponentAlignment(evaluatorOutput, Alignment.BOTTOM_RIGHT);

		return checkerLayout;
	}

	protected void updatePropertiesComponent(ExprBasic value) {
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
			updateEvaluator(selectedExpression);
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
