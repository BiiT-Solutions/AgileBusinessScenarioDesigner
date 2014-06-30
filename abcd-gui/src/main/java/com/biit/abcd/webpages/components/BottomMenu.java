package com.biit.abcd.webpages.components;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class BottomMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 6149788828670200504L;
	private IconButton treeDesignerButton, formVariables, diagramBuilderButton,
			expressionsEditorButton, ruleEditorButton, decissionTableButton;

	protected BottomMenu() {
		super();
		defineButtomMenu();
	}

	private void defineButtomMenu() {
		setWidth("100%");
		setHeight("80px");

		

		// Add Tree Designer button.
		treeDesignerButton = new IconButton(LanguageCodes.BOTTOM_MENU_TREE_DESIGNER, ThemeIcon.TREE_DESIGNER_PAGE,
				LanguageCodes.BOTTOM_MENU_TREE_DESIGNER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 6344185231722436896L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.TREE_DESIGNER);
					}
				});
		treeDesignerButton.setEnabled(false);
		addIconButton(treeDesignerButton);

		// Add Form Variables button.
		formVariables = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_VARIABLES, ThemeIcon.FORM_VARIABLES,
				LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = -6778800510554818966L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.FORM_VARIABLES);
					}
				});
		formVariables.setEnabled(false);
		addIconButton(formVariables);

		// Add Diagram Builder button.
		diagramBuilderButton = new IconButton(LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER,
				ThemeIcon.DIAGRAM_BUILDER_PAGE, LanguageCodes.BOTTOM_MENU_DIAGRAM_DESIGNER, IconSize.BIG,
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
		expressionsEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_EXPRESSION_EDITOR,
				ThemeIcon.EXPRESSION_EDITOR_PAGE, LanguageCodes.BOTTOM_MENU_EXPRESSION_EDITOR, IconSize.BIG,
				new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.EXPRESSION_EDITOR);
					}
				});
		expressionsEditorButton.setEnabled(false);
		addIconButton(expressionsEditorButton);

		// Add Drools Editor button.
		ruleEditorButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, ThemeIcon.DROOLS_RULE_EDITOR_PAGE,
				LanguageCodes.BOTTOM_MENU_DROOLS_EDITOR, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8212364503178436528L;

					@Override
					public void buttonClick(ClickEvent event) {
						changeView(WebMap.DROOLS_RULE_EDITOR);
					}
				});
		ruleEditorButton.setEnabled(false);
		addIconButton(ruleEditorButton);

		decissionTableButton = new IconButton(LanguageCodes.BOTTOM_MENU_DROOLS_TABLE_EDITOR, ThemeIcon.TABLE,
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
		// WebPageComponent nextPage = (WebPageComponent) ((ApplicationFrame)
		// UI.getCurrent()).getCurrentView();
	}

	public void updateButtons(boolean enableFormButtons) {
		if (treeDesignerButton != null) {
			treeDesignerButton.setEnabled(enableFormButtons);
		}
		if (formVariables != null) {
			formVariables.setEnabled(enableFormButtons);
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
}
