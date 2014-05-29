package com.biit.abcd.webpages.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class PopupWindow extends Window {
	private static final long serialVersionUID = 1189564721160097477L;

	public PopupWindow() {
		super();
	}

	public PopupWindow(String caption) {
		super(caption);
	}

	public PopupWindow(String caption, Component content) {
		super(caption, content);
	}

	public void showRelativeToComponent(Component component) {
		if (!component.isAttached()) {
			return;
		}
		UI.getCurrent().addWindow(this);
	}
}
