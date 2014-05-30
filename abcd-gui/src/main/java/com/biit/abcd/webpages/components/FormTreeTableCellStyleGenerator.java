package com.biit.abcd.webpages.components;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.TreeObjectTable.TreeObjectTableProperties;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Cell Style generator for FormTreeTable. Adds style for column name in each
 * row depending on the itemId.
 * 
 */
public class FormTreeTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 2280270027613134315L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		if (propertyId == TreeObjectTableProperties.ELEMENT_NAME) {
			if (itemId instanceof Form) {
				return "tree-cell-form";
			}
			if (itemId instanceof Category) {
				return "tree-cell-category";
			}
			if (itemId instanceof Group) {
				return "tree-cell-group";
			}
			if (itemId instanceof Question) {
				return "tree-cell-question";
			}
			if (itemId instanceof Answer) {
				return "tree-cell-answer";
			}
		}
		return "";
	}

}
