package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.form.entity.TreeObject;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * Creates a Vaadin Component composed by a label and an icon.
 */
public class TreeObjectWithIconComponent extends CustomComponent {
	private static final long serialVersionUID = 2524549021164976837L;
	private static final String CLASSNAME = "v-edit-cell-component";
	private static final String ICON_CLASSNAME = "v-tree-designer-icon";

	private CssLayout rootLayout;
	private IconButton treeObjectIcon;
	private Label textLabel;
	private TreeObject treeObject;
	private ThemeIcon themeIcon;

	public TreeObjectWithIconComponent(TreeObject treeObject, ThemeIcon icon, String iconTooltip) {
		this.treeObject = treeObject;
		this.themeIcon = icon;

		addStyleName(CLASSNAME);
		setImmediate(true);

		rootLayout = new CssLayout();
		rootLayout.setWidth(null);
		rootLayout.setHeight(null);
		rootLayout.setImmediate(true);

		if (icon != null) {
			treeObjectIcon = new IconButton(icon, IconSize.SMALL, iconTooltip);
			treeObjectIcon.addStyleName(ICON_CLASSNAME);
		}

		textLabel = new Label(treeObject.getName());
		textLabel.setSizeUndefined();

		rootLayout.addComponent(textLabel);
		if (icon != null) {
			rootLayout.addComponent(treeObjectIcon);
		}

		setCompositionRoot(rootLayout);
		setSizeUndefined();
	}

	@Override
	public void detach() {
		if (rootLayout.isConnectorEnabled()) {
			rootLayout.detach();
		}
	}

	public void addLayoutClickListener(LayoutClickListener listener) {
		rootLayout.addLayoutClickListener(listener);
	}

	public void removeLayoutClickListener(LayoutClickListener listener) {
		rootLayout.removeLayoutClickListener(listener);
	}

	public void addTreeObjectButtonClickListener(ClickListener listener) {
		if (treeObjectIcon != null) {
			treeObjectIcon.addClickListener(listener);
		}
	}

	public void removeTreeObjectButtonClickListener(ClickListener listener) {
		if (treeObjectIcon != null) {
			treeObjectIcon.removeClickListener(listener);
		}
	}

	public void setLabel(String label) {
		textLabel.setValue(label);
	}

	public void setLabel(TreeObject treeObject) {
		if (treeObject == null) {
			setLabel(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_NULL_VALUE));
		} else {
			setLabel(treeObject.getName());
		}
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}

	public ThemeIcon getThemeIcon() {
		return themeIcon;
	}

	public void update(TreeObject element) {
		// TODO Auto-generated method stub
		
	}

}
