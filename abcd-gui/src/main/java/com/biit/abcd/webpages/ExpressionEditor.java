package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaEditor;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class ExpressionEditor extends FormWebPageComponent {
	private static final long serialVersionUID = -156277380420304738L;

	public ExpressionEditor() {
		super();
		initContent();
	}

	private void initContent() {
		updateButtons(true);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(new FormulaEditor());

		getWorkingAreaLayout().addComponent(rootLayout);

	}

	@Override
	public void setForm(Form form) {
		// TODO Auto-generated method stub

	}

	@Override
	public Form getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {

	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}
}
