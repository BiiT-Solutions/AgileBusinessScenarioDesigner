package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SelectTableWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -3835385087067694004L;

	private VerticalLayout rootLayout;
	private SelectTableRuleTable selectRuleTable;

	public SelectTableWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	private Component generateComponent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);
		
		selectRuleTable = new SelectTableRuleTable();
		selectRuleTable.setSizeFull();
		
		for(TableRule tableRule: UserSessionHandler.getFormController().getForm().getTableRules()){
			selectRuleTable.addRow(tableRule);
		}

		selectRuleTable.sort();
		rootLayout.addComponent(selectRuleTable);

		return rootLayout;
	}
	
	public TableRule getSelectedTableRule(){
		return selectRuleTable.getSelectedTableRule();
	}
}
