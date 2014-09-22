package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

public class AcceptCancelClearWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 5798236038284261870L;
	private List<ClearElementsActionListener> clearListeners;
	protected IconButton clearButton;

	public interface ClearElementsActionListener {
		public void clearAction(AcceptCancelClearWindow window);
	}

	public AcceptCancelClearWindow() {
		super();
		setModal(true);
		clearListeners = new ArrayList<>();
	}

	public AcceptCancelClearWindow(Component content) {
		super(content);
		clearListeners = new ArrayList<>();
	}

	@Override
	protected void generateAcceptCancelButton() {
		super.generateAcceptCancelButton();
		clearButton = new IconButton(LanguageCodes.DELETE_BUTTON_CAPTION, ThemeIcon.DELETE,
				LanguageCodes.DELETE_BUTTON_TOOLTIP, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -6302237054661116415L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireClearActionListeners();
					}
				});
	}

	@Override
	protected void generateLayout(Component content) {
		super.generateLayout(content);
		buttonLayout.addComponent(clearButton);
	}

	public void addClearActionListener(ClearElementsActionListener listener) {
		clearListeners.add(listener);
	}

	public void removeClearActionListener(ClearElementsActionListener listener) {
		clearListeners.remove(listener);
	}

	protected void fireClearActionListeners() {
		for (ClearElementsActionListener listener : clearListeners) {
			listener.clearAction(this);
		}
	}

}
