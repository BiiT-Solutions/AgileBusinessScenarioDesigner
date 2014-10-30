package com.biit.abcd.webpages.elements.droolsresults;

import com.biit.abcd.core.drools.facts.inputform.SubmittedCategory;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
import com.biit.abcd.core.drools.facts.inputform.SubmittedGroup;
import com.biit.abcd.webpages.elements.droolsresults.DroolsTreeObjectTable.DroolsTreeObjectTableProperties;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Cell Style generator for FormTreeTable. Adds style for column name in each
 * row depending on the itemId.
 * 
 */
public class DroolsObjectTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 2280270027613134315L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		if (propertyId == DroolsTreeObjectTableProperties.ELEMENT_NAME) {
			if (itemId instanceof SubmittedForm) {
				return "tree-cell-form";
			}
			if (itemId instanceof SubmittedCategory) {
				return "tree-cell-category";
			}
			if (itemId instanceof SubmittedGroup) {
				return "tree-cell-group";
			}
			if (itemId instanceof SubmittedQuestion) {
				return "tree-cell-question";
			}
		}
		return "";
	}
}
