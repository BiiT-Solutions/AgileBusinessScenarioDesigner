package com.biit.abcd.webpages.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;

public class IconButton extends Button {
	private static final long serialVersionUID = -8287465276670542699L;
	private final static IconSize defaultIconSize = IconSize.SMALL;

	public IconButton(String icon, IconSize size, String tooltip) {
		super("");
		createButton(icon, defaultIconSize, tooltip);
		addStyleName("link");
	}

	public IconButton(String icon, String tooltip, IconSize size, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(String icon, String tooltip, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, defaultIconSize, tooltip);
		addStyleName("link");
	}

	public IconButton(String caption, String icon, String tooltip, IconSize size, ClickListener clickListener) {
		super(caption, clickListener);
		createButton(icon, size, tooltip);
	}

	public IconButton(String caption, String icon, String tooltip, ClickListener clickListener) {
		super(caption, clickListener);
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(String caption, String icon, String tooltip, IconSize size) {
		super(caption);
		createButton(icon, size, tooltip);
	}

	public IconButton(String caption, String icon, String tooltip) {
		super(caption);
		setIcon(icon, defaultIconSize);
		setDescription(tooltip);
	}

	public void setIcon(String icon) {
		setIcon(icon, defaultIconSize);
	}

	public void setIcon(String icon, IconSize size) {
		if (icon != null && icon.length() > 0) {
			if (!size.equals(IconSize.NULL)) {
				addStyleName(size.getSyle());
				setIcon(new ThemeResource(icon));
			}
		}
	}

	private void createButton(String icon, IconSize size, String tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
	}

}
