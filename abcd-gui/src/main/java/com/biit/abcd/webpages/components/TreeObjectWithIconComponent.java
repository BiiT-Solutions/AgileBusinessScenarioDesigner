package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.TreeObject;
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

	public TreeObjectWithIconComponent(TreeObject treeObject, ThemeIcons icon, String iconTooltip) {
		this.treeObject = treeObject;

		addStyleName(CLASSNAME);

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

}
