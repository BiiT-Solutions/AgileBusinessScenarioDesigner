package com.biit.abcd.webpages.elements.droolsrule;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionViewer;
import com.biit.abcd.webpages.elements.expressionviewer.ExpressionViewer.LayoutClickedListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ConditionActionEditor extends ExpressionEditorComponent {
	private static final long serialVersionUID = -7858734121952085269L;
	private ExpressionViewer conditionViewer;
	private ExpressionViewer actionViewer;
	private ExpressionViewer selectedViewer;

	@Override
	public VerticalLayout createViewersLayout() {
		VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setMargin(false);
		viewLayout.setSpacing(false);

		conditionViewer = new ExpressionViewer();
		conditionViewer.setSizeFull();
		conditionViewer.setFocused(true);
		conditionViewer.addLayoutClickedListener(new LayoutClickedListener() {

			@Override
			public void clickedAction(ExpressionViewer viewer) {
				selectedViewer = conditionViewer;
				updateSelectionStyles();
			}
		});

		Label conditionLabel = new Label(
				ServerTranslate.translate(LanguageCodes.DROOLS_RULES_EDITOR_CONDITION_LAYOUT_CAPTION));
		viewLayout.addComponent(conditionLabel);
		viewLayout.setComponentAlignment(conditionLabel, Alignment.BOTTOM_LEFT);
		viewLayout.addComponent(conditionViewer);

		actionViewer = new ExpressionViewer();
		actionViewer.setSizeFull();
		actionViewer.setFocused(true);
		actionViewer.addLayoutClickedListener(new LayoutClickedListener() {

			@Override
			public void clickedAction(ExpressionViewer viewer) {
				selectedViewer = actionViewer;
				updateSelectionStyles();
			}
		});

		Label actionLabel = new Label(
				ServerTranslate.translate(LanguageCodes.DROOLS_RULES_EDITOR_ACTION_LAYOUT_CAPTION));
		viewLayout.addComponent(actionLabel);
		viewLayout.setComponentAlignment(actionLabel, Alignment.BOTTOM_LEFT);
		viewLayout.addComponent(actionViewer);

		viewLayout.setExpandRatio(conditionLabel, 0.05f);
		viewLayout.setExpandRatio(conditionViewer, 0.45f);
		viewLayout.setExpandRatio(actionLabel, 0.05f);
		viewLayout.setExpandRatio(actionViewer, 0.45f);

		// First one is the default selected.
		selectedViewer = conditionViewer;
		updateSelectionStyles();
		return viewLayout;
	}

	@Override
	public void updateSelectionStyles() {
		if (actionViewer.getFormExpression() != null) {
			if (selectedViewer != actionViewer) {
				actionViewer.addStyleName("expression-unselected");
				conditionViewer.removeStyleName("expression-unselected");
			} else {
				actionViewer.removeStyleName("expression-unselected");
				conditionViewer.addStyleName("expression-unselected");
			}
		} else {
			actionViewer.addStyleName("expression-unselected");
			conditionViewer.addStyleName("expression-unselected");
		}
	}

	@Override
	public ExpressionViewer getSelectedViewer() {
		return selectedViewer;
	}

	public void refreshRuleEditor(Rule rule) {
		conditionViewer.removeAllComponents();
		actionViewer.removeAllComponents();
		if (rule != null) {
			conditionViewer.updateExpression(rule.getCondition());
			actionViewer.updateExpression(rule.getActions());
		}else{
			conditionViewer.updateExpression(null);
			actionViewer.updateExpression(null);
		}
	}

}
