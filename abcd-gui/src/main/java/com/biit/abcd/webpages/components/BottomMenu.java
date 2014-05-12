package com.biit.abcd.webpages.components;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public abstract class BottomMenu extends Panel {
	private Button formManagerButton, treeDesignerButton, diagramBuilderButton, droolsEditorButton;

	protected BottomMenu() {
		defineButtomMenu();
	}

	private void defineButtomMenu() {
		HorizontalLayout menuLayout = new HorizontalLayout();
		setContent(menuLayout);
		setWidth("100%");
		setHeight("80px");
		menuLayout.setSpacing(true);
		menuLayout.setWidth("100%");

		// Add FormManager button.
		formManagerButton = new IconButton(ThemeIcons.FORM_MANAGER_PAGE.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_FORM_MANAGER), IconSize.BIG, new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.FORM_MANAGER);
					}
				});
		menuLayout.addComponent(formManagerButton);
		menuLayout.setComponentAlignment(formManagerButton, Alignment.MIDDLE_CENTER);

		// Add Tree Designer button.
		treeDesignerButton = new IconButton(ThemeIcons.TREE_DESIGNER_PAGE.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_TREE_DESIGNER), IconSize.BIG, new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.TREE_DESIGNER);
					}
				});
		menuLayout.addComponent(treeDesignerButton);
		menuLayout.setComponentAlignment(treeDesignerButton, Alignment.MIDDLE_CENTER);

		// Add Diagram Builder button.
		diagramBuilderButton = new IconButton(ThemeIcons.DIAGRAM_BUILDER_PAGE.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER), IconSize.BIG, new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.DIAGRAM_BUILDER);
					}
				});
		menuLayout.addComponent(diagramBuilderButton);
		menuLayout.setComponentAlignment(diagramBuilderButton, Alignment.MIDDLE_CENTER);

		// Add Drools Editor button.
		droolsEditorButton = new IconButton(ThemeIcons.DROOLS_RULE_EDITOR_PAGE.getFile(),
				ServerTranslate.tr(LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR), IconSize.BIG, new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		droolsEditorButton.setEnabled(false);
		menuLayout.addComponent(droolsEditorButton);
		menuLayout.setComponentAlignment(droolsEditorButton, Alignment.MIDDLE_CENTER);

	}

	private void changeView(WebMap newView) {
		ApplicationFrame.navigateTo(newView);
		WebPageComponent nextPage = (WebPageComponent) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
		if (getSelectedForm() != null && nextPage != null && nextPage instanceof FormWebPageComponent) {
			((FormWebPageComponent) nextPage).setForm(getSelectedForm());
		}
	}

	public void updateButtons(boolean enableFormButtons) {
		if (formManagerButton != null) {
			formManagerButton.setEnabled(enableFormButtons);
		}
		if (treeDesignerButton != null) {
			treeDesignerButton.setEnabled(enableFormButtons);
		}
		if (diagramBuilderButton != null) {
			diagramBuilderButton.setEnabled(enableFormButtons);
		}
		if (droolsEditorButton != null) {

		}
	}

	public abstract void setSelectedForm(Form form);

	public abstract Form getSelectedForm();

}
