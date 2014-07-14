package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.TreeObject;

/**
 * This object is a variation of TreeObjectTable that can use multiselect. The icon is removed due to is not possible to
 * use component inside the table and multiselect with shift key together.
 */
public class TreeObjectTableMultiSelect extends TreeObjectTable {
	private static final long serialVersionUID = -820060659335684751L;

	public TreeObjectTableMultiSelect() {
		super();
		this.setMultiSelect(true);
	}

	@Override
	protected void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		setCellStyleGenerator(new FormTreeTableCellStyleGenerator());
	}

	/**
	 * Return a simple string with the element name.
	 */
	@Override
	protected Object createElementWithIcon(final TreeObject element) {
		return element.toString();
	}
}
