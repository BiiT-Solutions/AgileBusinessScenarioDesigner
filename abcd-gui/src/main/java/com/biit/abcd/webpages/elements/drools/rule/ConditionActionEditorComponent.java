package com.biit.abcd.webpages.elements.drools.rule;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.elements.expression.viewer.ConditionExpressionViewer;
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionEditorComponent;
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionViewer;
import com.biit.abcd.webpages.elements.expression.viewer.ExpressionViewer.LayoutClickedListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ConditionActionEditorComponent extends ExpressionEditorComponent {
	private static final long serialVersionUID = -7858734121952085269L;
	private ConditionExpressionViewer conditionViewer;
	private ExpressionViewer actionViewer;
	private ExpressionViewer selectedViewer;
	private List<SelectedViewerListener> selectedViewerListeners;

	public interface SelectedViewerListener {
		void viewerClicked(boolean isActionViewer);
	}

	public ConditionActionEditorComponent() {
		super();
		selectedViewerListeners = new ArrayList<>();
	}

	protected ConditionExpressionViewer createConditionExpressionViewer() {
		ConditionExpressionViewer expressionViewer = new ConditionExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}
	
	protected ExpressionViewer createExpressionViewer() {
		ExpressionViewer expressionViewer = new ExpressionViewer();
		expressionViewer.setSizeFull();
		expressionViewer.setFocused(true);
		return expressionViewer;
	}

	@Override
	public VerticalLayout createViewersLayout() {
		VerticalLayout viewLayout = new VerticalLayout();
		viewLayout.setSizeFull();
		viewLayout.setMargin(false);
		viewLayout.setSpacing(false);

		conditionViewer = createConditionExpressionViewer();
		conditionViewer.addLayoutClickedListener(new LayoutClickedListener() {

			@Override
			public void clickedAction(ExpressionViewer viewer) {
				selectedViewer = conditionViewer;
				updateSelectionStyles();
				launchViewerSelectedAction();
				enableAssignOperator();
			}
		});

		Label conditionLabel = new Label(
				ServerTranslate.translate(LanguageCodes.DROOLS_RULES_EDITOR_CONDITION_LAYOUT_CAPTION));
		viewLayout.addComponent(conditionLabel);
		viewLayout.setComponentAlignment(conditionLabel, Alignment.BOTTOM_LEFT);
		viewLayout.addComponent(conditionViewer);

		actionViewer = createExpressionViewer();
		actionViewer.addLayoutClickedListener(new LayoutClickedListener() {

			@Override
			public void clickedAction(ExpressionViewer viewer) {
				selectedViewer = actionViewer;
				updateSelectionStyles();
				launchViewerSelectedAction();
				enableAssignOperator();
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
		enableAssignOperator();
		return viewLayout;
	}

	protected void enableAssignOperator() {
		if (selectedViewer != null && selectedViewer.equals(actionViewer)) {
			enableAssignOperator(true);
		} else {
			enableAssignOperator(false);
		}
	}

	@Override
	public void updateSelectionStyles() {
		if (actionViewer.getExpressions() != null) {
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
			conditionViewer.updateExpression(rule.getConditions());
			actionViewer.updateExpression(rule.getActions());
		} else {
			conditionViewer.updateExpression(null);
			actionViewer.updateExpression(null);
		}
	}

	public ExpressionViewer getConditionViewer() {
		return conditionViewer;
	}

	public ExpressionViewer getActionViewer() {
		return actionViewer;
	}

	private void launchViewerSelectedAction() {
		for (SelectedViewerListener selectedViewerListener : selectedViewerListeners) {
			selectedViewerListener.viewerClicked(getSelectedViewer().equals(actionViewer));
		}
	}

	public void addSelectedViewerListener(SelectedViewerListener selectedViewerListener) {
		selectedViewerListeners.add(selectedViewerListener);
	}

	public void removeSelectedViewerListener(SelectedViewerListener selectedViewerListener) {
		selectedViewerListeners.remove(selectedViewerListener);
	}

}
