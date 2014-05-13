package com.biit.abcd.webpages.components;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

public abstract class BottomMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 6149788828670200504L;
	private IconButton formManagerButton, treeDesignerButton, diagramBuilderButton, droolsEditorButton;

	protected BottomMenu() {
		super();
		defineButtomMenu();
	}

	private void defineButtomMenu() {
		setWidth("100%");
		setHeight("80px");

		// Add FormManager button.
		formManagerButton = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_MANAGER, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 4002268252434768032L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.FORM_MANAGER);
					}
				});
		addIconButton(formManagerButton);

		// Add Tree Designer button.
		treeDesignerButton = new IconButton(LanguageCodes.BOTTOM_MENU_TREE_DESIGNER, ThemeIcons.TREE_DESIGNER_PAGE,
				LanguageCodes.BOTTOM_MENU_TREE_DESIGNER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 6344185231722436896L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.TREE_DESIGNER);
					}
				});
		addIconButton(treeDesignerButton);

		// Add Diagram Builder button.
		diagramBuilderButton = new IconButton(LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER,
				ThemeIcons.DIAGRAM_BUILDER_PAGE, LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER, IconSize.BIG,
				new ClickListener() {
					private static final long serialVersionUID = -6778800510554818966L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.DIAGRAM_BUILDER);
					}
				});
		addIconButton(diagramBuilderButton);

		// Add Drools Editor button.
		droolsEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR,
				ThemeIcons.DROOLS_RULE_EDITOR_PAGE, LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, IconSize.BIG,
				new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {

					}
				});
		droolsEditorButton.setEnabled(false);
		addIconButton(droolsEditorButton);

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
