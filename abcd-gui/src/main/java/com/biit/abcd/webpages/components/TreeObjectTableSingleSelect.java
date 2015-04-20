package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.form.entity.TreeObject;

/**
 * This object is a variation of TreeObjectTable that do not use the icons.
 */
public class TreeObjectTableSingleSelect extends TreeObjectTable {
	private static final long serialVersionUID = -820060659335684751L;

	public TreeObjectTableSingleSelect() {
		super();
	}

	@Override
	protected void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		setCellStyleGenerator(new TreeObjectTableCellStyleGenerator());
	}

	/**
	 * Return a simple string with the element name.
	 */
	@Override
	protected Object createElementWithIcon(final TreeObject element) {
		return element.toString();
	}
}
