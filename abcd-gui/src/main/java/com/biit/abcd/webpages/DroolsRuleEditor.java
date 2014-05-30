package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class DroolsRuleEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;
	private Form form;

	public DroolsRuleEditor() {
		super();
		initContent();
	}

	@Override
	protected void initContent() {
		updateButtons(true);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		// rootLayout.addComponent();

		getWorkingAreaLayout().addComponent(rootLayout);
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}
}