package com.biit.abcd.webpages.elements.drools.results;

import com.biit.abcd.webpages.elements.drools.results.DroolsTreeObjectTable.DroolsTreeObjectTableProperties;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
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
			if (itemId instanceof DroolsSubmittedForm) {
				return "tree-cell-form";
			}
			if (itemId instanceof DroolsSubmittedCategory) {
				return "tree-cell-category";
			}
			if (itemId instanceof DroolsSubmittedGroup) {
				return "tree-cell-group";
			}
			if (itemId instanceof DroolsSubmittedQuestion) {
				return "tree-cell-question";
			}
		}
		return "";
	}
}
