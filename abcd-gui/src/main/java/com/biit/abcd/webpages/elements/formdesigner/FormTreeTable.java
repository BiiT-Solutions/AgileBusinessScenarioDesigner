package com.biit.abcd.webpages.elements.formdesigner;

import java.util.List;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.components.ComponentCellRule;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.TreeObject;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public class FormTreeTable extends TreeObjectTable {
	private static final long serialVersionUID = 6016194810449244086L;

	public enum FormTreeTableProperties {
		ELEMENT_NAME, RULES
	};

	public FormTreeTable() {
		addContainerProperty(FormTreeTableProperties.RULES, Component.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_RULES), null, Align.LEFT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addItem(TreeObject element, TreeObject parent) {
		super.addItem(element, parent);
		if (element != null) {
			Set<Rule> assignedRules = UserSessionHandler.getFormController().getRulesAssignedToTreeObject(element);
			Set<ExpressionChain> expressionChains = UserSessionHandler.getFormController()
					.getFormExpressionChainsAssignedToTreeObject(element);

			ComponentCellRule rulesComponent = getRulesComponent(element, assignedRules, expressionChains);
			Item item = getItem(element);
			item.getItemProperty(FormTreeTableProperties.RULES).setValue(rulesComponent);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateItem(TreeObject element) {
		super.updateItem(element);
		Item item = getItem(element);
		if (item != null) {
			Set<Rule> assignedRules = UserSessionHandler.getFormController().getRulesAssignedToTreeObject(element);
			Set<ExpressionChain> expressionChains = UserSessionHandler.getFormController()
					.getFormExpressionChainsAssignedToTreeObject(element);

			ComponentCellRule rulesComponent = getRulesComponent(element, assignedRules, expressionChains);
			item.getItemProperty(FormTreeTableProperties.RULES).setValue(rulesComponent);
		}
	}

	public ComponentCellRule getRulesComponent(TreeObject element, Set<Rule> rules,
			Set<ExpressionChain> expressionChains) {
		ComponentCellRule component = new ComponentCellRule();
		component.update(rules, expressionChains);
		component.registerTouchCallBack(this, element);
		return component;
	}

	public String getRulesAsText(List<Rule> rules) {
		String ruleText = new String();
		for (Rule rule : rules) {
			ruleText += rule.getName();
		}
		return ruleText;
	}
}
