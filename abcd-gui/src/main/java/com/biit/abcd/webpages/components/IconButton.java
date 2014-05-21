package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Button;

public class IconButton extends Button {
	private static final long serialVersionUID = -8287465276670542699L;
	private final static IconSize defaultIconSize = IconSize.SMALL;

	public IconButton(ThemeIcons icon, IconSize size, LanguageCodes tooltip) {
		super("");
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(ThemeIcons icon, LanguageCodes tooltip, IconSize size, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, size, tooltip);
		addStyleName("link");
	}

	public IconButton(ThemeIcons icon, LanguageCodes tooltip, ClickListener clickListener) {
		super("", clickListener);
		createButton(icon, defaultIconSize, tooltip);
		addStyleName("link");
	}

	public IconButton(LanguageCodes caption, ThemeIcons icon, LanguageCodes tooltip, IconSize size,
			ClickListener clickListener) {
		super(ServerTranslate.tr(caption),clickListener);
		createButton(icon, size, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcons icon, LanguageCodes tooltip, ClickListener clickListener) {
		super(ServerTranslate.tr(caption),clickListener);
		createButton(icon, defaultIconSize, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcons icon, LanguageCodes tooltip, IconSize size) {
		super(ServerTranslate.tr(caption));
		createButton(icon, size, tooltip);
	}

	public IconButton(LanguageCodes caption, ThemeIcons icon, LanguageCodes tooltip) {
		super(ServerTranslate.tr(caption));
		createButton(icon, defaultIconSize, tooltip);
	}

	public void setIcon(ThemeIcons icon) {
		setIcon(icon, defaultIconSize);
	}

	public void setIcon(ThemeIcons icon, IconSize size) {
		if (icon != null && (!size.equals(IconSize.NULL))) {
			addStyleName(size.getSyle());
			setIcon(icon.getThemeResource());

		}
	}

	private void createButton(ThemeIcons icon, IconSize size, LanguageCodes tooltip) {
		setIcon(icon, size);
		setDescription(ServerTranslate.tr(tooltip));
		setImmediate(true);
	}
}
