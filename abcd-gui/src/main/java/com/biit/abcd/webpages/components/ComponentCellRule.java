package com.biit.abcd.webpages.components;

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

import java.util.Set;

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

	public void update(Set<Rule> rules, Set<ExpressionChain> expressions) {
		clear();
		for (Rule rule : rules) {
			addRule(rule);
		}
		for (ExpressionChain expression : expressions) {
			addExpression(expression);
		}
	}

	public void addExpression(final ExpressionChain expression) {
		addIconButton(ThemeIcon.EXPRESSION_EDITOR_PAGE, new ClickListener() {
			private static final long serialVersionUID = 8228702328434889430L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.EXPRESSION_EDITOR);
				ExpressionEditor expressionEditor = (ExpressionEditor) ((ApplicationFrame) UI.getCurrent())
						.getCurrentView();
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
