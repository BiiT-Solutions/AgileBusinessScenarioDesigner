package com.biit.abcd.webpages.components;

import java.util.List;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.DroolsRuleEditor;
import com.biit.abcd.webpages.ExpressionEditor;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public class ComponentCellRule extends ComponentCell {
	private static final long serialVersionUID = -5962905982968281894L;

	public void update(List<Rule> rules, List<ExpressionChain> expressions) {
		clear();
		for (Rule rule : rules) {
			addRule(rule);
		}
		for(ExpressionChain expression: expressions){
			addExpression(expression);
		}
	}

	public void addExpression(final ExpressionChain expression) {
		addIconButton(ThemeIcon.EXPRESSION_EDITOR_PAGE, new ClickListener() {
			private static final long serialVersionUID = 8228702328434889430L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.EXPRESSION_EDITOR);
				ExpressionEditor expressionEditor = (ExpressionEditor) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
				expressionEditor.selectComponent(expression);
			}
		});
		addLabel(expression.getName());
	}

	public void addRule(final Rule rule) {
		addIconButton(ThemeIcon.DROOLS_RULE_EDITOR_PAGE, new ClickListener() {
			private static final long serialVersionUID = -1034508395874639479L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.DROOLS_RULE_EDITOR);
				DroolsRuleEditor ruleEditor = (DroolsRuleEditor) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
				ruleEditor.selectComponent(rule);
			}
		});
		addLabel(rule.getName());
	}
}
