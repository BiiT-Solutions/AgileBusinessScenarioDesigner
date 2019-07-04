package com.biit.abcd.webpages.elements.form.designer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.utils.UsesOfElement;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.components.ComponentCellRule;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public class FormTreeTable extends TreeObjectTable {
	private static final long serialVersionUID = 6016194810449244086L;
	private UsesOfElement usesOfElement;

	public enum FormTreeTableProperties {
		USES, RULES
	};

	public FormTreeTable() {
		addContainerProperty(FormTreeTableProperties.USES, Integer.class, 0,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_USES), null, Align.CENTER);
		addContainerProperty(FormTreeTableProperties.RULES, Component.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_RULES), null, Align.LEFT);

		setColumnExpandRatio(TreeObjectTableProperties.ELEMENT_NAME, 0.45f);
		setColumnExpandRatio(FormTreeTableProperties.USES, 0.1f);
		setColumnExpandRatio(FormTreeTableProperties.RULES, 0.45f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addItem(TreeObject element, TreeObject parent, boolean select) {
		super.addItem(element, parent, select);
		if (element != null) {
			Set<Rule> assignedRules = UserSessionHandler.getFormController().getRulesAssignedToTreeObject(element);
			Set<ExpressionChain> expressionChains = UserSessionHandler.getFormController()
					.getFormExpressionChainsAssignedToTreeObject(element);

			ComponentCellRule rulesComponent = getRulesComponent(element, assignedRules, expressionChains);
			Item item = getItem(element);
			item.getItemProperty(FormTreeTableProperties.RULES).setValue(rulesComponent);
			item.getItemProperty(FormTreeTableProperties.USES).setValue(getElementUses(element));
			setValue(element);
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
			item.getItemProperty(FormTreeTableProperties.USES).setValue(getElementUses(element));
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

	private int getElementUses(TreeObject element) {
		if (usesOfElement == null) {
			usesOfElement = new UsesOfElement((Form) element.getAncestor(Form.class));
		}
		return usesOfElement.getUsesOfElement(element);
	}

	public Set<Object> getCollapsedStatus(TreeObject treeObject) {
		Set<Object> collapsedItems = new HashSet<>();
		getCollapsedStatus(treeObject,collapsedItems);
		return collapsedItems;
	}
	
	private void getCollapsedStatus(TreeObject treeObject, Set<Object> collapsedItems){
		if(isCollapsed(treeObject)){
			collapsedItems.add(treeObject);
		}
		for(TreeObject child: treeObject.getChildren()){
			getCollapsedStatus(child, collapsedItems);
		}
	}

	public void setCollapsedStatus(TreeObject treeObject, Set<Object> collapsedStatus) {
		setCollapsed(treeObject, false);
		for(TreeObject child: treeObject.getChildren()){
			setCollapsedStatus(child, collapsedStatus);
		}
		setCollapsed(treeObject, collapsedStatus.contains(treeObject));
	}
}
