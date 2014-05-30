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
	private IconButton formManagerButton, treeDesignerButton, diagramBuilderButton, expressionsEditorButton,
			ruleEditorButton, decissionTableButton;

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
		formManagerButton.setEnabled(false);
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
		treeDesignerButton.setEnabled(false);
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
		diagramBuilderButton.setEnabled(false);
		addIconButton(diagramBuilderButton);

		// Add calculus expresion editor.
		expressionsEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_EXPRESSION_EDITOR, ThemeIcons.EXPRESSION_EDITOR_PAGE,
				LanguageCodes.BOTTOM_MENU_EXPRESSION_EDITOR, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.EXPRESSION_EDITOR);
					}
				});
		expressionsEditorButton.setEnabled(false);
		addIconButton(expressionsEditorButton);

		// Add Drools Editor button.
		ruleEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, ThemeIcons.DROOLS_RULE_EDITOR_PAGE,
				LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.DROOLS_RULE_EDITOR);
					}
				});
		ruleEditorButton.setEnabled(false);
		addIconButton(ruleEditorButton);

		decissionTableButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_TABLE_EDITOR, ThemeIcons.PAPER,
				LanguageCodes.BOTTOM_MENU_DROOLS_TABLE_EDITOR, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = -2494460723270342409L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.DECISSION_TABLE_EDITOR);
					}
				});
		decissionTableButton.setEnabled(false);
		addIconButton(decissionTableButton);
	}

	private void changeView(WebMap newView) {
		ApplicationFrame.navigateTo(newView);
		WebPageComponent nextPage = (WebPageComponent) ((ApplicationFrame) UI.getCurrent()).getCurrentView();
		if (getSelectedForm() != null && nextPage != null && nextPage instanceof FormWebPageComponent) {
			((FormWebPageComponent) nextPage).setForm(getSelectedForm());
		}
	}

	public void updateButtons(boolean enableFormButtons) {
		formManagerButton.setEnabled(true);

		if (treeDesignerButton != null) {
			treeDesignerButton.setEnabled(enableFormButtons);
		}
		if (diagramBuilderButton != null) {
			diagramBuilderButton.setEnabled(enableFormButtons);
		}
		if (expressionsEditorButton != null) {
			expressionsEditorButton.setEnabled(enableFormButtons);
		}
		if (ruleEditorButton != null) {
			ruleEditorButton.setEnabled(enableFormButtons);
		}
		if (decissionTableButton != null) {
			decissionTableButton.setEnabled(enableFormButtons);
		}
	}

	public abstract void setSelectedForm(Form form);

	public abstract Form getSelectedForm();

}
