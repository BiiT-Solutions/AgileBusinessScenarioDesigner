package com.biit.abcd.webpages.components;

import com.biit.abcd.webpages.components.TreeObjectTable.TreeObjectTableProperties;
import com.biit.form.BaseAnswer;
import com.biit.form.BaseCategory;
import com.biit.form.BaseForm;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Cell Style generator for FormTreeTable. Adds style for column name in each
 * row depending on the itemId.
 * 
 */
public class TreeObjectTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 2280270027613134315L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		if (propertyId == TreeObjectTableProperties.ELEMENT_NAME) {
			if (itemId instanceof BaseForm) {
				return "tree-cell-form";
			}
			if (itemId instanceof BaseCategory) {
				return "tree-cell-category";
			}
			if (itemId instanceof BaseRepeatableGroup) {
				return "tree-cell-group";
			}
			if (itemId instanceof BaseQuestion) {
				return "tree-cell-question";
			}
			if (itemId instanceof BaseAnswer) {
				return "tree-cell-answer";
			}
		}
		return "";
	}

}
