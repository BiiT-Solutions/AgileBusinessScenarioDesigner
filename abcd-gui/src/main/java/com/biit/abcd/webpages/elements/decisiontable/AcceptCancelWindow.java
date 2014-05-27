package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.IconSize;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AcceptCancelWindow extends Window {

	private static final long serialVersionUID = 8796193085149771811L;
	private List<AcceptActionListener> acceptListeners;
	private List<CancelActionListener> cancelListeners;
	protected IconButton acceptButton;
	protected IconButton cancelButton;
	private Component contentComponent;

	public interface AcceptActionListener {
		public void acceptAction(AcceptCancelWindow window);
	}

	public interface CancelActionListener {
		public void cancelAction(AcceptCancelWindow window);
	}

	public AcceptCancelWindow() {
		super();
		acceptListeners = new ArrayList<AcceptCancelWindow.AcceptActionListener>();
		cancelListeners = new ArrayList<AcceptCancelWindow.CancelActionListener>();
	}

	public AcceptCancelWindow(Component content) {
		super();
		acceptListeners = new ArrayList<AcceptCancelWindow.AcceptActionListener>();
		cancelListeners = new ArrayList<AcceptCancelWindow.CancelActionListener>();
		setContent(content);
	}

	@Override
	public void setContent(Component content) {
		// NOTE Vaadin WTF. Super initialization will call this function
		// even if no content is passed.
		this.contentComponent = content;
		generateLayout(contentComponent);
	}

	@Override
	public Component getContent() {
		return contentComponent;
	}

	protected void generateAcceptCancelButton() {
		acceptButton = new IconButton(LanguageCodes.WINDOW_NEWFORM_SAVEBUTTON_LABEL, ThemeIcons.ACCEPT,
				LanguageCodes.WINDOW_NEWFORM_SAVEBUTTON_TOOLTIP, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = 6785334478985006998L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireAcceptActionListeners();
					}
				});
		cancelButton = new IconButton(LanguageCodes.WINDOW_NEWFORM_CANCELBUTTON_LABEL, ThemeIcons.CANCEL, null,
				IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -6302237054661116415L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireCancelActionListeners();
						close();
					}
				});
	}

	protected void generateLayout(Component content) {
		generateAcceptCancelButton();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(null);
		buttonLayout.setSpacing(true);

		buttonLayout.addComponent(acceptButton);
		buttonLayout.addComponent(cancelButton);

		if (content != null) {
			rootLayout.addComponent(content);
			rootLayout.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
			rootLayout.setExpandRatio(content, 1.0f);
		}
		rootLayout.addComponent(buttonLayout);
		rootLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(buttonLayout, 0.0f);
		
		super.setContent(rootLayout);
	}

	public void addAcceptAcctionListener(AcceptActionListener listener) {
		acceptListeners.add(listener);
	}

	public void removeAcceptAcctionListener(AcceptActionListener listener) {
		acceptListeners.remove(listener);
	}

	public void addCancelAcctionListener(CancelActionListener listener) {
		cancelListeners.add(listener);
	}

	public void removeAcceptAcctionListener(CancelActionListener listener) {
		cancelListeners.remove(listener);
	}

	private void fireAcceptActionListeners() {
		for (AcceptActionListener listener : acceptListeners) {
			listener.acceptAction(this);
		}
	}

	private void fireCancelActionListeners() {
		for (CancelActionListener listener : cancelListeners) {
			listener.cancelAction(this);
		}
	}
}
