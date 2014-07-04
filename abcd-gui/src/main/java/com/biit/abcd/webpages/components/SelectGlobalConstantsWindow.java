package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SelectGlobalConstantsWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -4212298247094386855L;

	private HorizontalLayout rootLayout;
	private VerticalLayout selectionComponent;

	private GlobalConstantsListSelect constantSelection;

	public SelectGlobalConstantsWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(true);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	public Component generateComponent() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);
		setCaption(ServerTranslate.translate(LanguageCodes.EXPRESSION_GLOBAL_CONSTANT_WINDOW_CAPTION));

		selectionComponent = new VerticalLayout();
		selectionComponent.setSizeFull();
		selectionComponent.setImmediate(true);

		rootLayout.addComponent(selectionComponent);

		initializeVariableSelection();
		selectionComponent.addComponent(constantSelection);

		return rootLayout;
	}

	private void initializeVariableSelection() {
		constantSelection = new GlobalConstantsListSelect();
	}

	public GlobalVariable getValue() {
		if (constantSelection.getValue() == null) {
			return null;
		}
		return (GlobalVariable) constantSelection.getValue();
	}

	public void setValue(GlobalVariable globalVariable) {
		constantSelection.setValue(globalVariable);
	}

}
