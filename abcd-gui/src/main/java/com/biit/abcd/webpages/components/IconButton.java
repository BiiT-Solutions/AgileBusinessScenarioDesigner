package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Button;

public class IconButton extends Button {
	private static final long serialVersionUID = -8287465276670542699L;
	private final static IconSize defaultIconSize = IconSize.SMALL;

	public IconButton(ThemeIcon icon, IconSize size, LanguageCodes tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(ThemeIcon icon, IconSize size, String tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(ThemeIcon icon, LanguageCodes tooltip, IconSize size, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(ThemeIcon icon, LanguageCodes tooltip, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, defaultIconSize, tooltip);
		addStyleName("link");
	}

	public IconButton(LanguageCodes caption, ThemeIcon icon, LanguageCodes tooltip, IconSize size,
			ClickListener clickListener) {
		super(ServerTranslate.translate(caption), clickListener);
		createButton(icon, size, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcon icon, LanguageCodes tooltip, ClickListener clickListener) {
		super(ServerTranslate.translate(caption), clickListener);
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcon icon, LanguageCodes tooltip, IconSize size) {
		super(ServerTranslate.translate(caption));
		createButton(icon, size, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcon icon, LanguageCodes tooltip) {
		super(ServerTranslate.translate(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcon icon, String tooltip) {
		super(ServerTranslate.translate(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public void setIcon(ThemeIcon icon) {
		setIcon(icon, defaultIconSize);
	}

	public void setIcon(ThemeIcon icon, IconSize size) {
		if (icon != null && (!size.equals(IconSize.NULL))) {
			addStyleName(size.getSyle());
			setIcon(icon.getThemeResource());

		}
	}

	private void createButton(ThemeIcon icon, IconSize size, LanguageCodes tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
		setImmediate(true);
	}

	private void createButton(ThemeIcon icon, IconSize size, String tooltip) {
		setIcon(icon, size);
		setDescription(tooltip);
		setImmediate(true);
	}

	public void setDescription(LanguageCodes tooltip) {
		setDescription(ServerTranslate.translate(tooltip));
	}
}
