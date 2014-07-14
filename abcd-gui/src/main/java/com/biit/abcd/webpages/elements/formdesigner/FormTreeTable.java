package com.biit.abcd.webpages.elements.formdesigner;

import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public class FormTreeTable extends TreeObjectTable {
	private static final long serialVersionUID = 6016194810449244086L;

	protected enum FormTreeTableProperties {
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
			List<Rule> assignedRules = UserSessionHandler.getFormController().getRulesAssignedToTreeObject(element);
			FormTreeTableRuleComponent rulesComponent = getRulesComponent(assignedRules);		
			
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
			List<Rule> assignedRules = UserSessionHandler.getFormController().getRulesAssignedToTreeObject(element);
			FormTreeTableRuleComponent rulesComponent = getRulesComponent(assignedRules);			
			
			item.getItemProperty(FormTreeTableProperties.RULES).setValue(rulesComponent);
		}
	}
	
	public FormTreeTableRuleComponent getRulesComponent(List<Rule> rules){
		if(rules.isEmpty()){
			return null;
		}else{
			FormTreeTableRuleComponent component = new FormTreeTableRuleComponent();
			for(Rule rule: rules){
				component.addRuleReference(rule);
			}
			return component;
		}
	}
	
	public String getRulesAsText(List<Rule> rules){
		String ruleText = new String();
		for(Rule rule: rules){
			ruleText += rule.getName();
		}
		return ruleText;
	}
}
